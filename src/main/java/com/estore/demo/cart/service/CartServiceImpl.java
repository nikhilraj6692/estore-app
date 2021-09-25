package com.estore.demo.cart.service;

import com.estore.demo.cart.domain.CartItem;
import com.estore.demo.cart.domain.CartItems;
import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.common.domain.ErrorVO;
import com.estore.demo.common.exceptions.BusinessException;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.product.domain.Product;
import com.estore.demo.product.repo.IProductRepository;
import com.estore.demo.cart.repo.ICartRepository;
import com.estore.demo.cart.vo.CartItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartServiceImpl implements ICartService {

    private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ICartRepository cartRepository;

    /**
     * Method to add pproduct to the cart
     *
     * @param cartItemVO
     */
    @Override
    public void addProductToCart(CartItemVO cartItemVO) {
        log.debug("add product to cart starts");
        String userId = loggedInUser.getUserId();

        /*
        check if the product id which is to be added/updated is equal to or less than product stock count or
         not
         */
        Product fetchedproduct = productRepository.findOne(cartItemVO.getProduct().getId());

        if (null == fetchedproduct) {
            throw new BusinessException(new ErrorVO(ApplicationConstants.PRODUCT_ID_NOT_FOUND,
                    new Object[]{cartItemVO.getProduct().getId()}));
        } else {
            if (cartItemVO.getCount() > fetchedproduct.getStock()) {
                throw new BusinessException(new ErrorVO(ApplicationConstants.PRODUCT_NOT_IN_STOCK,
                        new Object[]{cartItemVO.getProduct().getId()}));
            }
        }

        CartItems items = cartRepository.findById(userId);

        //create a cart for the user
        if (null == items) {
            items = new CartItems();
            items.setId(userId);
        }

        //check if the product is present in the cart or not..if yes, then it would be an update operation
        Optional<CartItem> cartItemOptional = items.getCartItemList().stream().
                filter(cartItem -> cartItem.getProduct().getId().equals(cartItemVO.getProduct().getId()))
                .findAny();

        if (cartItemOptional.isPresent()) {
            //update operation
            CartItem cartItem = cartItemOptional.get();
            cartItem.setCount(cartItemVO.getCount());
            cartItem.getProduct().setPrice(cartItemVO.getProduct().getPrice());
        } else {
            //add operation
            CartItem item = new CartItem();
            Product product = new Product();
            product.setPrice(cartItemVO.getProduct().getPrice());
            product.setId(cartItemVO.getProduct().getId());
            item.setProduct(product);
            item.setCount(cartItemVO.getCount());
            items.getCartItemList().add(item);
        }

        //save cart item
        cartRepository.save(items);

        log.debug("add product to cart ends");
    }

}
