package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "order_items", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductsEntity productsEntity;
    @Basic
    @Column(name = "created_at")
    private Date createdAt;

}
