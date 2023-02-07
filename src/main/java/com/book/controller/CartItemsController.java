package com.book.controller;

import com.book.dto.CartItemsDTO;
import com.book.model.CartItemsEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.service.CartItemsService;
import com.book.service.ProductsService;
import com.book.service.UserService;
import com.book.util.JwtUtils;
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
    private final JwtUtils jwtUtils;
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
    public ResponseEntity<String> addCartItems(@RequestBody CartItemsDTO cartItemsDTO) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(cartItemsDTO.getJwt());

        if (usersEntity.isPresent()) {
            Optional<ProductsEntity> productsEntity = productsService.findProductById(cartItemsDTO.getProductId());

            if (productsEntity.isEmpty()) {
                return new ResponseEntity<>("Failed to add new cart items", HttpStatus.BAD_REQUEST);
            }

            CartItemsEntity cartItemsEntity = new CartItemsEntity();
            cartItemsEntity.setUserId(usersEntity.get().getId());
            cartItemsEntity.setProductsEntity(productsEntity.get());
            cartItemsEntity.setQuantity(cartItemsDTO.getQuantity());

            if (Boolean.TRUE.equals(cartItemsService.saveCartItem(cartItemsEntity))) {
                return new ResponseEntity<>("Adding new cart items is successful", HttpStatus.OK);
            }
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
