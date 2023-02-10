package com.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products", schema = "e-commerce")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler", "cartItemsEntityList"})
@NoArgsConstructor
public class ProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne(cascade = CascadeType.PERSIST)
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
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private UsersEntity usersEntity;
    @OneToMany(mappedBy = "productsEntity")
    private List<CartItemsEntity> cartItemsEntityList;
}
