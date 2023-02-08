package com.book.service;

import com.book.dto.JwtModel;
import com.book.dto.OrdersDTO;

public interface OrdersServiceInterface {
    Boolean createOrderFromCart(JwtModel jwtModel);
    Boolean createOrderFromProductId(OrdersDTO ordersDTO);
}
