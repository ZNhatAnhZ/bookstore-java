package com.book.service;

import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;

import java.util.List;
import java.util.Optional;

public interface ProductReviewsServiceInterface {
    Optional<List<ProductReviewsEntity>> findAllByProductId(int productsEntityId);
    Boolean saveProductReview(ProductReviewDTO productReviewDTO);
    Optional<Integer> getAverageRatingByProductId(int productsEntityId);
    Optional<List<ProductReviewsEntity>> findAll();
}
