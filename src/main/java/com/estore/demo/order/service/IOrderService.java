package com.estore.demo.order.service;

import com.estore.demo.order.vo.OrderVO;

public interface IOrderService {
    String orderNow(OrderVO orderVO);

    String checkout(OrderVO orderVO);
}
