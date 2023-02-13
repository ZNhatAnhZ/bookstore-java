package com.book.dto;

import lombok.Data;

@Data
public class CartItemsDTO extends JwtModel{
    private final Integer productId;
    private final Integer quantity;
}
