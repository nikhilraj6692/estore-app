package com.estore.demo.order.rest;

import com.estore.demo.common.aspectj.RequiredPermission;
import com.estore.demo.common.domain.StatusVO;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.order.service.IOrderService;
import com.estore.demo.order.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
Rest resource to place order
 */
@RestController
@RequestMapping(path = "/v1/orders")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Autowired
    private IOrderService orderService;

    @Autowired
    private MessageSource messageSource;

    /*
    Rest resource to buy an item without adding it to the cart
     */
    @PutMapping(path = "/ordernow", consumes = "application/json", produces = "application/json")
    @RequiredPermission(capabilityId = "Order.PlaceOrder", permission = RequiredPermission.PermissionValue.UPDATE)
    public ResponseEntity<StatusVO> buyNow(@Valid @RequestBody OrderVO orderVO) {
        log.debug("Buy now method starts");
        String orderID = orderService.orderNow(orderVO);

        StatusVO statusVO = new StatusVO();
        statusVO.setStatus(HttpStatus.CREATED);
        statusVO.setMessage(messageSource.getMessage
                (ApplicationConstants.ORDER_PLACED_SUCCESSFULLY, null, Locale.getDefault()));
        Map<String, String> map = new HashMap<>();
        map.put("orderID", orderID);
        statusVO.setDetails(map);
        return ResponseEntity.status(200).body(statusVO);
    }

    /*
    Rest resource to check out cart and place an order
     */
    @PutMapping(path = "/checkout", consumes = "application/json", produces = "application/json")
    @RequiredPermission(capabilityId = "Order.PlaceOrder", permission = RequiredPermission.PermissionValue.UPDATE)
    public ResponseEntity<StatusVO> checkOut(@Valid @RequestBody OrderVO orderVO) {
        log.debug("Buy now method starts");
        String orderID = orderService.checkout(orderVO);

        StatusVO statusVO = new StatusVO();
        statusVO.setStatus(HttpStatus.CREATED);
        statusVO.setMessage(messageSource.getMessage
                (ApplicationConstants.ORDER_PLACED_SUCCESSFULLY, null, Locale.getDefault()));
        Map<String, String> map = new HashMap<>();
        map.put("orderID", orderID);
        statusVO.setDetails(map);
        return ResponseEntity.status(200).body(statusVO);
    }
}
