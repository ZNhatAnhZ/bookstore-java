package com.book.service;

import com.book.model.ProductsEntity;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductsServiceInterface {
    Optional<Page<ProductsEntity>> findAllProducts(int page, int size);
    Optional<Page<ProductsEntity>> findAllProductsBySort(int page, int size, String sort);
    Optional<Page<ProductsEntity>> findAllProductsSortedByIdDesc(int page, int size);
    Optional<Page<ProductsEntity>> findAllProductsSortedByPriceAsc(int page, int size);
    Optional<Page<ProductsEntity>> findAllProductsSortedByPriceDesc(int page, int size);
    Optional<Page<ProductsEntity>> findAllProductsSortedByOrderItemsCount(int page, int size);
    Optional<Page<ProductsEntity>> findAllProductsOfCategory(int page, int size, String category);
    Optional<ProductsEntity> findProductById(int id);
    Optional<Page<ProductsEntity>> findAllByProductNameContainingIgnoreCase(int page, int size, String productName);
    Optional<List<ProductsEntity>> getRecommendedProducts(int userId) throws IOException;
}
