package com.book.dto;

import lombok.Data;

@Data
public class OrdersDTO {
    private final String jwt;
    private final int productId;
    private final int quantity;
}
