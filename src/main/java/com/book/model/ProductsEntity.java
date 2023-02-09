package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products", schema = "e-commerce")
@Getter
@Setter
@NoArgsConstructor
public class ProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "product_category")
    private CategoryEntity categoryEntity;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "product_price")
    private Integer productPrice;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "product_details")
    private String productDetails;
    @Basic
    @Column(name = "product_photo")
    private String productPhoto;
    @Basic
    @Column(name = "provider_id")
    private Integer providerId;
}
