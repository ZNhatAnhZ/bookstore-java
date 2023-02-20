package com.book.service;

import com.book.dto.RatingObjectDTO;
import com.book.dto.NewProductDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductReviewsEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.repository.ProductsRepository;
import com.book.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductsService implements ProductsServiceInterface{
    private final ProductsRepository productsRepository;
    private final CategoryService categoryService;
    private final ProductReviewsService productReviewsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;
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

        List<RatingObjectDTO> result = getRatingObjectDTO(productReviewsEntityList.get());

        ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, pythonProgramLocation, result.toString(), String.valueOf(userId));
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<Integer> productIdList = new ObjectMapper().readValue(in.readLine(), List.class);
        return Optional.of(productsRepository.findAllById(productIdList));
    }

    @Override
    public List<RatingObjectDTO> getRatingObjectDTO(List<ProductReviewsEntity> productReviewsEntityList) {
        List<RatingObjectDTO> ratingObjectDTOList = productReviewsEntityList.stream().map(productReviewsEntity -> new RatingObjectDTO(productReviewsEntity.getUsersEntity().getId(), productReviewsEntity.getProductId(), productReviewsEntity.getRating())).toList();

        Map<List<Integer>, Double> averageRatingOfUserAndProduct = ratingObjectDTOList.stream().collect(Collectors.groupingBy(ratingObjectDTO -> List.of(ratingObjectDTO.getUserId(),ratingObjectDTO.getProductId()), Collectors.averagingInt(RatingObjectDTO::getRating)));

        return averageRatingOfUserAndProduct.entrySet().stream().map(e -> new RatingObjectDTO(e.getKey().get(0), e.getKey().get(1), e.getValue().intValue())).toList();
    }

    @Override
    @Transactional
    public Optional<List<ProductsEntity>> findAllByUsersEntityId(int usersEntityId) {
        return productsRepository.findAllByUsersEntityId(usersEntityId);
    }

    @Override
    @Transactional
    public Boolean createProduct(String authHeader, NewProductDTO newProductDTO) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(authHeader.substring(7));
        Optional<CategoryEntity> existCategoryEntity = categoryService.findCategoryEntityByCategoryName(newProductDTO.getCategoryName());
        if (usersEntity.isPresent()) {
            CategoryEntity categoryEntity = new CategoryEntity();

            if (existCategoryEntity.isEmpty()) {
                categoryEntity.setCategoryName(newProductDTO.getCategoryName());
            } else {
                categoryEntity = existCategoryEntity.get();
            }

            ProductsEntity productsEntity = new ProductsEntity();
            productsEntity.setQuantity(newProductDTO.getQuantity());
            productsEntity.setProductDetails(newProductDTO.getProductDetails());
            productsEntity.setProductPhoto(newProductDTO.getProductPhoto());
            productsEntity.setProductName(newProductDTO.getProductName());
            productsEntity.setProductPrice(newProductDTO.getProductPrice());
            productsEntity.setCategoryEntity(categoryEntity);
            productsEntity.setUsersEntity(usersEntity.get());

            try {
                productsRepository.save(productsEntity);
                return true;
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean modifyProduct(NewProductDTO newProductDTO) {
        Optional<CategoryEntity> existCategoryEntity = categoryService.findCategoryEntityByCategoryName(newProductDTO.getCategoryName());
        Optional<ProductsEntity> existProductsEntity = productsRepository.findById(newProductDTO.getProductId());

        if (existProductsEntity.isPresent()) {
            CategoryEntity categoryEntity = new CategoryEntity();

            if (existCategoryEntity.isEmpty()) {
                categoryEntity.setCategoryName(newProductDTO.getCategoryName());
            } else {
                categoryEntity = existCategoryEntity.get();
            }

            if (newProductDTO.getQuantity() != null && newProductDTO.getProductDetails() != null && newProductDTO.getProductPhoto() != null && newProductDTO.getProductName() != null && newProductDTO.getProductPrice() != null) {
                existProductsEntity.get().setQuantity(newProductDTO.getQuantity());
                existProductsEntity.get().setProductDetails(newProductDTO.getProductDetails());
                existProductsEntity.get().setProductPhoto(newProductDTO.getProductPhoto());
                existProductsEntity.get().setProductName(newProductDTO.getProductName());
                existProductsEntity.get().setProductPrice(newProductDTO.getProductPrice());
                existProductsEntity.get().setCategoryEntity(categoryEntity);
            }

            try {
                productsRepository.save(existProductsEntity.get());
                return true;
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean deleteProduct(NewProductDTO newProductDTO) {
        try {
            productsRepository.deleteById(newProductDTO.getProductId());
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }
}
