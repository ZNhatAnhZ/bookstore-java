package com.book.repository;

import com.book.model.ProductReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewsRepository extends JpaRepository<ProductReviewsEntity, Integer> {
    List<ProductReviewsEntity> findAllByProductId(int productsEntityId);
}
