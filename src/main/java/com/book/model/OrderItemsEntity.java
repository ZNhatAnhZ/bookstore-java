package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

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
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "created_at")
    private Date createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemsEntity that = (OrderItemsEntity) o;
        return id == that.id && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId, createdAt);
    }
}
