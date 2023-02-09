package com.book.service;

import com.book.dto.RatingObjectDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductReviewsEntity;
import com.book.model.ProductsEntity;
import com.book.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsService implements ProductsServiceInterface{
    private final ProductsRepository productsRepository;
    private final CategoryService categoryService;
    private final ProductReviewsService productReviewsService;
    @Value("${pythonInterpreter}")
    private String pythonInterpreter;
    @Value("${pythonProgramLocation}")
    private String pythonProgramLocation;

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

    @Override
    @Transactional
    public Optional<List<ProductsEntity>> getRecommendedProducts(int userId) throws IOException {
        Optional<List<ProductReviewsEntity>> productReviewsEntityList = productReviewsService.findAll();

        if (productReviewsEntityList.isEmpty()) {
            return Optional.empty();
        }

        List<RatingObjectDTO> ratingObjectDTOList = productReviewsEntityList.get().stream().map(productReviewsEntity -> new RatingObjectDTO(productReviewsEntity.getUsersEntity().getId(), productReviewsEntity.getProductId(), productReviewsEntity.getRating())).toList();

        Map<List<Integer>, Double> averageRatingOfUserAndProduct = ratingObjectDTOList.stream().collect(Collectors.groupingBy(ratingObjectDTO -> List.of(ratingObjectDTO.getUserId(),ratingObjectDTO.getProductId()), Collectors.averagingInt(RatingObjectDTO::getRating)));

        List<RatingObjectDTO> result = averageRatingOfUserAndProduct.entrySet().stream().map(e -> new RatingObjectDTO(e.getKey().get(0), e.getKey().get(1), e.getValue().intValue())).toList();

        ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, pythonProgramLocation, result.toString(), String.valueOf(userId));
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<Integer> productIdList = new ObjectMapper().readValue(in.readLine(), List.class);
        return Optional.of(productsRepository.findAllById(productIdList));
    }
}
