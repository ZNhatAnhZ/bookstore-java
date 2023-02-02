package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemsEntity that = (CartItemsEntity) o;
        return id == that.id && Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId, quantity);
    }
}
