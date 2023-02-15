package com.book.controller;

import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;
import com.book.service.ProductReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/productReview")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductReviewController {
    private final ProductReviewsService productReviewsService;
    @GetMapping("/getProductReviewByProductId")
    public ResponseEntity<List<ProductReviewsEntity>> getProductReviewByProductId(@RequestParam int productId) {
        Optional<List<ProductReviewsEntity>> result = productReviewsService.findAllByProductId(productId);

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAverageRating")
    public ResponseEntity<Integer> getAverageRating(@RequestParam int productId) {
        Optional<Integer> result = productReviewsService.getAverageRatingByProductId(productId);

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addProductReview")
    public ResponseEntity<String> addProductReview(@RequestHeader(name = "Authorization") String authHeader, @RequestBody ProductReviewDTO productReviewDTO) {
        Boolean result = productReviewsService.saveProductReview(authHeader, productReviewDTO);

        if (Boolean.TRUE.equals(result)) {
            return new ResponseEntity<>("adding a new product review successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("failed to add a new product review", HttpStatus.BAD_REQUEST);
    }
}
