package com.book.service;

import com.book.exception.InvalidQuantityException;
import com.book.model.CartItemsEntity;
import com.book.model.OrderItemsEntity;
import com.book.model.OrdersEntity;
import com.book.model.ProductsEntity;
import com.book.repository.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrderItemsService implements OrderItemsServiceInterface{
    private final OrderItemsRepository orderItemsRepository;
    private final CartItemsService cartItemsService;
    @Override
    @Transactional
    public Boolean createOrderItemsEntityByCartItemEntityList(OrdersEntity ordersEntity, List<CartItemsEntity> cartItemsEntityList) throws InvalidQuantityException {
        List<OrderItemsEntity> orderItemsEntityList = new ArrayList<>();

        for (CartItemsEntity cartItemsEntity : cartItemsEntityList) {
            if (cartItemsEntity.getProductsEntity().getQuantity() > cartItemsEntity.getQuantity()) {
                OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
                orderItemsEntity.setQuantity(cartItemsEntity.getQuantity());
                orderItemsEntity.setOrdersEntity(ordersEntity);
                orderItemsEntity.setProductsEntity(cartItemsEntity.getProductsEntity());
                orderItemsEntity.setCreatedAt(Date.valueOf(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));

                orderItemsEntity.getProductsEntity().setQuantity(orderItemsEntity.getProductsEntity().getQuantity()-cartItemsEntity.getQuantity());
                orderItemsEntityList.add(orderItemsEntity);
            } else {
                throw new InvalidQuantityException("Quantity higher than product stock");
            }
        }

        try {
            orderItemsRepository.saveAll(orderItemsEntityList);
            cartItemsService.deleteCartItemByCartItemList(cartItemsEntityList);

            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean createOrderItemsEntityByProductEntity(OrdersEntity ordersEntity,ProductsEntity productsEntity, int quantity) throws InvalidQuantityException {
        if (productsEntity.getQuantity() < quantity) {
            throw new InvalidQuantityException("Quantity higher than product stock");
        }

        productsEntity.setQuantity(productsEntity.getQuantity()-quantity);
        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setOrdersEntity(ordersEntity);
        orderItemsEntity.setProductsEntity(productsEntity);
        orderItemsEntity.setQuantity(quantity);
        orderItemsEntity.setCreatedAt(Date.valueOf(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));

        try {
            orderItemsRepository.save(orderItemsEntity);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }
}
