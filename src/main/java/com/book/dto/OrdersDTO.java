package com.book.dto;

import lombok.Data;

@Data
public class OrdersDTO extends JwtModel{
    private final int productId;
    private final int quantity;
}
