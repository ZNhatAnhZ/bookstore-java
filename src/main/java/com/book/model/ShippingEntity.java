package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "shipping", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class ShippingEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "order_id")
    private Integer orderId;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "required_date")
    private Date requiredDate;
    @Basic
    @Column(name = "shipped_date")
    private Date shippedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingEntity that = (ShippingEntity) o;
        return id == that.id && Objects.equals(orderId, that.orderId) && Objects.equals(status, that.status) && Objects.equals(requiredDate, that.requiredDate) && Objects.equals(shippedDate, that.shippedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, status, requiredDate, shippedDate);
    }
}
