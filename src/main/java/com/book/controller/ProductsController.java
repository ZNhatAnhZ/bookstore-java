package com.book.controller;

import com.book.dto.ProductsDTO;
import com.book.model.ProductsEntity;
import com.book.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/getProducts")
    public ResponseEntity<ProductsDTO> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(defaultValue = "default") String sort) {
        Optional<Page<ProductsEntity>> result = productsService.findAllProductsBySort(page, size, sort);
        return getProductsDTOResponseEntity(result);
    }

    @GetMapping("/getProductsByCategory")
    public ResponseEntity<ProductsDTO> getProductsByCategory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size, @RequestParam String categoryName) {
        Optional<Page<ProductsEntity>> result = productsService.findAllProductsOfCategory(page, size, categoryName);
        return getProductsDTOResponseEntity(result);
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductsEntity> getProductById(@RequestParam int id) {
        Optional<ProductsEntity> result = productsService.findProductById(id);

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ProductsEntity(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<ProductsDTO> getProductByName(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size, @RequestParam String productName) {
        Optional<Page<ProductsEntity>> result = productsService.findAllByProductNameContainingIgnoreCase(page, size, productName);

        return getProductsDTOResponseEntity(result);
    }

    @GetMapping("/getRecommendedProducts")
    public ResponseEntity<List<ProductsEntity>> getRecommendedProducts(@RequestParam int userId) throws IOException {
        Optional<List<ProductsEntity>> result = productsService.getRecommendedProducts(userId);

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ProductsDTO> getProductsDTOResponseEntity(Optional<Page<ProductsEntity>> result) {
        ProductsDTO productsDTO = new ProductsDTO();

        if (result.isPresent()) {
            productsDTO.setProductsEntityList(result.get().toList());
            productsDTO.setCurrentPage(result.get().getNumber());
            productsDTO.setTotalPages(result.get().getTotalPages());
            productsDTO.setTotalItems(result.get().getTotalElements());

            return new ResponseEntity<>(productsDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(productsDTO, HttpStatus.BAD_REQUEST);
    }
}
