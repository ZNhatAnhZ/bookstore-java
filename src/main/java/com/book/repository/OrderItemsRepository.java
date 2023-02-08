package com.book.repository;

import com.book.model.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository  extends JpaRepository<OrderItemsEntity, Integer> {
}
