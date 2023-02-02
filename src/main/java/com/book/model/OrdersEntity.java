package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "order_by")
    private Integer orderBy;
    @Basic
    @Column(name = "total_amount")
    private Integer totalAmount;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "created_at")
    private Date createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity that = (OrdersEntity) o;
        return id == that.id && Objects.equals(orderBy, that.orderBy) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(status, that.status) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderBy, totalAmount, status, createdAt);
    }
}
