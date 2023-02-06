package com.book.repository;

import com.book.model.CartItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItemsEntity, Integer> {
    Optional<List<CartItemsEntity>> findCartItemsEntitiesByUserId(int id);
}
