package com.book.service;

import com.book.model.CartItemsEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemsServiceInterface {
    Optional<List<CartItemsEntity>> getCartItemsEntitiesByUserId(int id);
}
