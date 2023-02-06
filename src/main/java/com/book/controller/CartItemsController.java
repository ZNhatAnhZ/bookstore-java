package com.book.controller;

import com.book.model.CartItemsEntity;
import com.book.model.UsersEntity;
import com.book.service.CartItemsService;
import lombok.Getter;
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

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartItemsEntity>> getCartItems(@RequestParam int id) {
        Optional<List<CartItemsEntity>> result = cartItemsService.getCartItemsEntitiesByUserId(id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<CartItemsEntity>(), HttpStatus.OK);

    }
}
