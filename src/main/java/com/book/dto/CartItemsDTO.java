package com.book.dto;

import lombok.Data;

@Data
public class CartItemsDTO extends JwtModel{
    private final int productId;
    private final int quantity;
}
