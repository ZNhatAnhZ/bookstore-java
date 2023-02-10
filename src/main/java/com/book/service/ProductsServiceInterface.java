package com.book.service;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductsServiceInterface {
    @Transactional
    Optional<Page<ProductsEntity>> findAllProducts(int page, int size);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsBySort(int page, int size, String sort);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsSortedByIdDesc(int page, int size);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsSortedByPriceAsc(int page, int size);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsSortedByPriceDesc(int page, int size);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsSortedByOrderItemsCount(int page, int size);

    @Transactional
    Optional<Page<ProductsEntity>> findAllProductsOfCategory(int page, int size, String category);

    @Transactional
    Optional<ProductsEntity> findProductById(int id);

    @Transactional
    Optional<Page<ProductsEntity>> findAllByProductNameContainingIgnoreCase(int page, int size, String productName);

    @Transactional
    Optional<List<ProductsEntity>> getRecommendedProducts(int userId) throws IOException;

    @Transactional
    Optional<List<ProductsEntity>> findAllByUsersEntityId(int usersEntityId);

    @Transactional
    Boolean createProduct(String authHeader, NewProductDTO newProductDTO);

    @Transactional
    Boolean modifyProduct(NewProductDTO newProductDTO);

    @Transactional
    Boolean deleteProduct(NewProductDTO newProductDTO);
}
