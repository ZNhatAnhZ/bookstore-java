package com.book.service;

import com.book.dto.CartItemsDTO;
import com.book.model.CartItemsEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemsServiceInterface {
    Optional<List<CartItemsEntity>> getCartItemsEntitiesByUserId(int id);
    Boolean saveCartItem(String jwt, CartItemsDTO cartItemsDTO);
    Boolean deleteCartItemById(int id);
    Boolean deleteCartItemByCartItemList(List<CartItemsEntity> cartItemsEntityList);
}
