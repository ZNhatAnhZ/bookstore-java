package com.book.controller;

import com.book.dto.ProductsDTO;
import com.book.model.ProductsEntity;
import com.book.service.ProductsService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
