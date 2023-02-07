package com.book.service;

import com.book.model.ProductsEntity;

import java.util.List;
import java.util.Optional;

public interface ProductsServiceInterface {
    Optional<List<ProductsEntity>> findAllProducts();
    Optional<ProductsEntity> findProductById(int id);
}
