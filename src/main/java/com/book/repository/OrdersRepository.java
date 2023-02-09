package com.book.repository;

import com.book.model.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository  extends JpaRepository<OrdersEntity, Integer> {
}
