package com.book.service;

import com.book.dto.OrdersDTO;

public interface OrdersServiceInterface {
    Boolean createOrderFromCart(String authHeader);
    Boolean createOrderFromProductId(String authHeader, OrdersDTO ordersDTO);
}
