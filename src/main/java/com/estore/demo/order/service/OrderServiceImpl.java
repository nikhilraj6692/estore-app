package com.estore.demo.order.service;

import com.estore.demo.cart.domain.CartItem;
import com.estore.demo.cart.domain.CartItems;
import com.estore.demo.cart.repo.ICartRepository;
import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.common.domain.ErrorVO;
import com.estore.demo.common.exceptions.BusinessException;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.EventName;
import com.estore.demo.notification.domain.NotificationAlertVO;
import com.estore.demo.notification.service.INotificationService;
import com.estore.demo.order.domain.*;
import com.estore.demo.order.repo.IOrderRepository;
import com.estore.demo.order.repo.IPaymentRepository;
import com.estore.demo.order.vo.OrderVO;
import com.estore.demo.product.domain.Product;
import com.estore.demo.product.repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Service layer to do order related transactions
 */
@Component
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private INotificationService notificationService;

    /*
    Service to order an item without adding it to the cart
     */
    @Override
    public String orderNow(OrderVO orderVO) {
        String userId = loggedInUser.getUserId();

        /*
        check that the id in order item matches with the product id currently present in the database,
        the original price is same as the price provided by seller and quantity is less or same that the stock
        of the product with the seller.
         */
        List<Product> products = productRepository.findByIdIn
                (orderVO.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList()));

        Map<String, Product> map = products.stream().collect
                (Collectors.toMap(product -> product.getId(), Function.identity()));

        for (OrderItem item : orderVO.getOrderItems()) {
            Product product = map.get(item.getId());
            if (null == product || product.getPrice() != item.getPrice() || product.getStock() < item.getQty()) {
                throw new BusinessException(new ErrorVO(ApplicationConstants.INVALID_ORDER_DETAILS));
            }

            //else set appropriate parameters from product
            item.setSellerId(product.getSellerId());
            item.setTax(item.getTax());
            item.setTotalPrice(item.getTotalPrice());
            item.setOtherCharges(item.getOtherCharges());

            //also reduce product stock now
            product.setStock(product.getStock() - item.getQty());
        }

        Order order = new Order();
        String orderID = UUID.randomUUID().toString();
        order.setId(orderID);
        order.setOrderItems(orderVO.getOrderItems());
        order.setLastUpdatedDate(new Date());
        order.setBillingAddress(orderVO.getAddress2());
        order.setUserId(userId);
        order.setOrderCreatedDate(new Date());
        order.setShippingAddress(orderVO.getAddress1());

        ShipmentDetails details = new ShipmentDetails();
        details.setShipmentStatus(ShipmentStatus.ORDERED);
        order.setShipmentDetails(details);

        PaymentDetail detail = null;

        if (orderVO.getPaymentDetail() instanceof Card) {
            detail = new Card();
            Card cardVO = (Card) orderVO.getPaymentDetail();
            ((Card) detail).setCardNumber(cardVO.getCardNumber());
            ((Card) detail).setExpiryMonth(cardVO.getExpiryMonth());
            ((Card) detail).setExpiryYear(cardVO.getExpiryYear());
        } else if (orderVO.getPaymentDetail() instanceof UPI) {
            detail = new UPI();
            UPI upiVO = (UPI) orderVO.getPaymentDetail();
            ((UPI) detail).setUpiAddress(upiVO.getUpiAddress());
            ((UPI) detail).setUpiChannel(upiVO.getUpiChannel());
        }

        detail.setUserId(userId);
        detail.setOrderId(orderID);
        detail.setPaymentStatus(PaymentStatus.AWAITING_VERIFICATION);

        /*
        save order
         */
        orderRepository.save(order);

        //save payment
        paymentRepository.save(detail);

        //save all products with affected stock or update...doing save for temporary purpose
        productRepository.save(products);

        //send notification
        NotificationAlertVO alertVO = new NotificationAlertVO();
        alertVO.setEntityId("Buy Now");
        //hardcoded email id...it shoudl be taken from profile
        alertVO.setEmailId("rajputnikhil433@gmail.com");
        alertVO.setEventName(EventName.PLACEORDER);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        alertVO.setData(data);

        //asynchronously send notification
        notificationService.sendNotification(alertVO);

        return orderID;
    }

    /*
    Service to checkout and place an order
     */
    @Override
    public String checkout(OrderVO orderVO) {
        //clear cart for the product ordered
        String userId = loggedInUser.getUserId();
        CartItems items = cartRepository.findById(userId);

        Set<String> productIdsOrdered = orderVO.getOrderItems().stream().
                map(OrderItem::getId).collect(Collectors.toSet());


        if (items.getCartItemList().isEmpty()) {
            throw new BusinessException(new ErrorVO(ApplicationConstants.CART_EMPTY));
        }

        Iterator<CartItem> itemItr = items.getCartItemList().iterator();
        while (itemItr.hasNext()) {
            //remove item as cart is checked out
            if (productIdsOrdered.contains(itemItr.next().getProduct().getId())) {
                itemItr.remove();
            }
        }

        String orderID = orderNow(orderVO);

        cartRepository.save(items);

        return orderID;
    }
}
