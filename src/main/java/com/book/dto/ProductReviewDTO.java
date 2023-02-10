package com.book.dto;

import lombok.Data;

@Data
public class ProductReviewDTO extends JwtModel{
    private final String comment;
    private final Integer rating;
    private final int productId;
}
