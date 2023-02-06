package com.book.service;

import com.book.model.CartItemsEntity;
import com.book.repository.CartItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartItemsService implements CartItemsServiceInterface{
    private final CartItemsRepository cartItemsRepository;

    @Override
    public Optional<List<CartItemsEntity>> getCartItemsEntitiesByUserId(int id) {
        return cartItemsRepository.findCartItemsEntitiesByUserId(id);
    }

    @Override
    public Boolean saveCartItem(CartItemsEntity cartItemsEntity) {
        try {
            cartItemsRepository.save(cartItemsEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean deleteCartItemById(int id) {
        try {
            cartItemsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
