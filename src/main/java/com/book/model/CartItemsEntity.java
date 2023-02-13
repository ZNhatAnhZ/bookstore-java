package com.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cart_items", schema = "e-commerce", catalog = "")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@NoArgsConstructor
public class CartItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductsEntity productsEntity;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
}
