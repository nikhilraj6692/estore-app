package com.estore.demo.cart.rest;

import com.estore.demo.common.aspectj.RequiredPermission;
import com.estore.demo.common.domain.StatusVO;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.cart.service.ICartService;
import com.estore.demo.cart.vo.CartItemVO;
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
import java.util.Locale;

@RestController
@RequestMapping(path = "/v1/cart")
public class CartResource {
    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    @Autowired
    private ICartService cartService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Rest resource to add products to the cart
     *
     * @param cartItemVO
     * @return ResponseEntity<StatusVO>
     */
    @PutMapping(path = "/addtocart", consumes = "application/json", produces = "application/json")
    @RequiredPermission(capabilityId = "Cart.AddToCart", permission = RequiredPermission.PermissionValue.UPDATE)
    public ResponseEntity<StatusVO> addProductToCart(@Valid @RequestBody CartItemVO cartItemVO) {
        log.debug("Add product to cart starts");
        cartService.addProductToCart(cartItemVO);

        StatusVO statusVO = new StatusVO();
        statusVO.setStatus(HttpStatus.CREATED);
        statusVO.setMessage(messageSource.getMessage
                (ApplicationConstants.CART_UPDATED_SUCCESSFULLY, null, Locale.getDefault()));
        log.debug("Add product to cart ends");
        return ResponseEntity.status(200).body(statusVO);
    }
}
