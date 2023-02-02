package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "shop", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class ShopEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "shop_owner_id")
    private Integer shopOwnerId;
    @Basic
    @Column(name = "revenue")
    private Integer revenue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopEntity that = (ShopEntity) o;
        return id == that.id && Objects.equals(shopOwnerId, that.shopOwnerId) && Objects.equals(revenue, that.revenue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopOwnerId, revenue);
    }
}
