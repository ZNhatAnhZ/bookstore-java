package com.book.dto;

import lombok.Data;

@Data
public class RatingObjectDTO {
    private Integer userId;
    private Integer productId;
    private Integer rating;

    public RatingObjectDTO(Integer userId, Integer productId, Integer rating) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "[" + userId + ", " + productId + ", " + rating +"]";
    }
}
