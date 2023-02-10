package com.book.repository;

import com.book.model.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<ProductsEntity, Integer> {
    @Query(
            value = "select products.id, products.product_category, products.product_name, products.product_price, products.quantity, products.product_details, products.product_photo, products.provider_id from `e-commerce`.products left join `e-commerce`.order_items on order_items.product_id = products.id group by products.id order by count(order_items.product_id) DESC",
            countQuery = "select count(*) from products",
            nativeQuery = true)
    Page<ProductsEntity> findAllByTotalSold(Pageable pageable);
    Page<ProductsEntity> findAllByCategoryEntityId(int id, Pageable pageable);
    Page<ProductsEntity> findAllByProductNameContainingIgnoreCase(Pageable pageable, String productName);
    Optional<List<ProductsEntity>> findAllByUsersEntityId(int usersEntityId);
    @Modifying
    @Query(value = "delete from ProductsEntity p where p.id = :id")
    void deleteProductsEntityById(int id);
}
