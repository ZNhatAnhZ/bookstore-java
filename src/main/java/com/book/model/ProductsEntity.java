package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "products", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class ProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "product_category")
    private Integer productCategory;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsEntity that = (ProductsEntity) o;
        return id == that.id && Objects.equals(productCategory, that.productCategory) && Objects.equals(productName, that.productName) && Objects.equals(productPrice, that.productPrice) && Objects.equals(quantity, that.quantity) && Objects.equals(productDetails, that.productDetails) && Objects.equals(productPhoto, that.productPhoto) && Objects.equals(providerId, that.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCategory, productName, productPrice, quantity, productDetails, productPhoto, providerId);
    }
}
