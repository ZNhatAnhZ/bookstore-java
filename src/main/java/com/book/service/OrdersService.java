package com.book.service;

import com.book.dto.OrdersDTO;
import com.book.model.CartItemsEntity;
import com.book.model.OrdersEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrdersService implements OrdersServiceInterface{
    private final OrdersRepository ordersRepository;
    private final UserService userService;
    private final OrderItemsService orderItemsService;
    private final CartItemsService cartItemsService;
    private final ProductsService productsService;
    @Override
    @Transactional
    public Boolean createOrderFromCart(String authHeader) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(authHeader.substring(7));

        if (usersEntity.isPresent()) {
            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setUsersEntity(usersEntity.get());
            ordersEntity.setStatus("completed");
            ordersEntity.setCreatedAt(Date.valueOf(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));


            Optional<List<CartItemsEntity>> cartItemsEntityList = cartItemsService.getCartItemsEntitiesByUserId(usersEntity.get().getId());

            if (cartItemsEntityList.isPresent() && !cartItemsEntityList.get().isEmpty()) {
                AtomicInteger totalAmount = new AtomicInteger();
                cartItemsEntityList.get().forEach(e-> totalAmount.addAndGet((e.getQuantity() * e.getProductsEntity().getProductPrice())));

                ordersEntity.setTotalAmount(totalAmount.intValue());

                try {
                    OrdersEntity savedOrdersEntity = ordersRepository.save(ordersEntity);
                    orderItemsService.createOrderItemsEntityByCartItemEntityList(savedOrdersEntity, cartItemsEntityList.get());

                    return true;
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }

        return false;
    }

    @Override
    @Transactional
    public Boolean createOrderFromProductId(String authHeader, OrdersDTO ordersDTO) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(authHeader.substring(7));
        Optional<ProductsEntity> productsEntity = productsService.findProductById(ordersDTO.getProductId());

        if (usersEntity.isPresent() && productsEntity.isPresent()) {
            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setStatus("completed");
            ordersEntity.setUsersEntity(usersEntity.get());
            ordersEntity.setCreatedAt(Date.valueOf(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));
            ordersEntity.setTotalAmount(productsEntity.get().getProductPrice()*ordersDTO.getQuantity());


            try {
                OrdersEntity savedOrdersEntity = ordersRepository.save(ordersEntity);
                orderItemsService.createOrderItemsEntityByProductEntity(savedOrdersEntity, productsEntity.get(), ordersDTO.getQuantity());

                return true;
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return false;
    }
}
