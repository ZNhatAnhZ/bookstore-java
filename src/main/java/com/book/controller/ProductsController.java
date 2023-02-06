package com.book.controller;

import com.book.model.ProductsEntity;
import com.book.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductsEntity>> getAllProducts() {
        Optional<List<ProductsEntity>> result = productsService.findAllProducts();

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(result.get(), HttpStatus.BAD_REQUEST);
    }
}
