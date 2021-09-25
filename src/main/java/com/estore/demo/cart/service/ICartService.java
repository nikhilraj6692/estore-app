package com.estore.demo.cart.service;

import com.estore.demo.cart.vo.CartItemVO;

public interface ICartService {

    void addProductToCart(CartItemVO cartItemVO);
}
