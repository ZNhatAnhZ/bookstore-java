package com.book.service;

import com.book.model.CategoryEntity;
import com.book.model.ProductsEntity;
import com.book.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsService implements ProductsServiceInterface{
    private final ProductsRepository productsRepository;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProducts(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size)));
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsBySort(int page, int size, String sort) {
        Optional<Page<ProductsEntity>> result;
        switch (sort) {
            case "newest" -> result = findAllProductsSortedByIdDesc(page, size);
            case "price_asc" -> result = findAllProductsSortedByPriceAsc(page, size);
            case "price_desc" -> result = findAllProductsSortedByPriceDesc(page, size);
            case "best_seller" -> result = findAllProductsSortedByOrderItemsCount(page, size);
            default -> result = findAllProducts(page, size);
        }
        return result;
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsSortedByIdDesc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsSortedByPriceAsc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").ascending())));
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsSortedByPriceDesc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").descending())));
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsSortedByOrderItemsCount(int page, int size) {
        return Optional.of(productsRepository.findAllByTotalSold(PageRequest.of(page, size)));
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllProductsOfCategory(int page, int size, String category) {
        Optional<CategoryEntity> categoryEntity = categoryService.findCategoryEntityByCategoryName(category);

        if (categoryEntity.isPresent()) {
            return Optional.of(productsRepository.findAllByCategoryEntityId(categoryEntity.get().getId(), PageRequest.of(page, size)));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<ProductsEntity> findProductById(int id) {
        return productsRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Page<ProductsEntity>> findAllByProductNameContainingIgnoreCase(int page, int size, String productName) {
        return Optional.ofNullable(productsRepository.findAllByProductNameContainingIgnoreCase(PageRequest.of(page, size), productName));
    }
}
