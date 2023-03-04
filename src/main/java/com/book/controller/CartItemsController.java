package com.book.controller;

import com.book.dto.CartItemsDTO;
import com.book.model.CartItemsEntity;
import com.book.service.impl.CartItemsService;
import com.book.service.impl.ProductsService;
import com.book.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartItemsController {
    private final CartItemsService cartItemsService;
    private final UserService userService;
    private final ProductsService productsService;

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartItemsEntity>> getCartItems(@RequestParam int id) {
        Optional<List<CartItemsEntity>> result = cartItemsService.getCartItemsEntitiesByUserId(id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @PostMapping("/addCartItems")
    public ResponseEntity<String> addCartItems(@RequestHeader(name = "Authorization") String authHeader, @RequestBody CartItemsDTO cartItemsDTO) {
        if (Boolean.TRUE.equals(cartItemsService.saveCartItem(authHeader, cartItemsDTO))) {
            return new ResponseEntity<>("Adding new cart items is successful", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed to add new cart items", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteCartItems")
    public ResponseEntity<String> deleteCartItems(@RequestBody CartItemsDTO cartItemsDTO) {
        if (Boolean.TRUE.equals(cartItemsService.deleteCartItemById(cartItemsDTO.getProductId()))) {
            return new ResponseEntity<>("Delete cart item successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed to delete cart item", HttpStatus.BAD_REQUEST);
    }
}
