package com.book.dto;

import lombok.Data;

@Data
public class OrdersDTO extends JwtModel{
    private final Integer productId;
    private final Integer quantity;
}
