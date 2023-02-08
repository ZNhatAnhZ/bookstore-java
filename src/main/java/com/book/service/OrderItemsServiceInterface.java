package com.book.service;

import com.book.exception.InvalidQuantityException;
import com.book.model.CartItemsEntity;
import com.book.model.OrdersEntity;
import com.book.model.ProductsEntity;

import java.util.List;

public interface OrderItemsServiceInterface {
    Boolean createOrderItemsEntityByCartItemEntityList(OrdersEntity ordersEntity, List<CartItemsEntity> cartItemsEntityList) throws InvalidQuantityException;
    Boolean createOrderItemsEntityByProductEntity(OrdersEntity ordersEntity, ProductsEntity productsEntity, int quantity) throws InvalidQuantityException;
}
