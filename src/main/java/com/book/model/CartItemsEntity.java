package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cart_items", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class CartItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductsEntity productsEntity;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
}
