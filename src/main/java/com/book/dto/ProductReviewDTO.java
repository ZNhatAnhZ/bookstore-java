package com.book.dto;

import lombok.Data;

@Data
public class ProductReviewDTO {
    private final String jwt;
    private final String comment;
    private final Integer rating;
    private final int productId;
}
