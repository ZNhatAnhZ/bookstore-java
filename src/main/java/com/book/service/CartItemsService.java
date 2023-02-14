package com.book.service;

import com.book.model.CartItemsEntity;
import com.book.repository.CartItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CartItemsService implements CartItemsServiceInterface{
    private final CartItemsRepository cartItemsRepository;

    @Override
    @Transactional
    public Optional<List<CartItemsEntity>> getCartItemsEntitiesByUserId(int id) {
        return cartItemsRepository.findCartItemsEntitiesByUserId(id);
    }

    @Override
    @Transactional
    public Boolean saveCartItem(CartItemsEntity cartItemsEntity) {
        try {
            cartItemsRepository.save(cartItemsEntity);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean deleteCartItemById(int id) {
        try {
            cartItemsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean deleteCartItemByCartItemList(List<CartItemsEntity> cartItemsEntityList) {
        try {
            cartItemsRepository.deleteAll(cartItemsEntityList);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }
}
