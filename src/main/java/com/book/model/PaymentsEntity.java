package com.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "payments", schema = "e-commerce", catalog = "")
@Getter
@Setter
@NoArgsConstructor
public class PaymentsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "payment_type")
    private String paymentType;
    @Basic
    @Column(name = "order_id")
    private Integer orderId;
    @Basic
    @Column(name = "amount")
    private Integer amount;
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
        PaymentsEntity that = (PaymentsEntity) o;
        return id == that.id && Objects.equals(paymentType, that.paymentType) && Objects.equals(orderId, that.orderId) && Objects.equals(amount, that.amount) && Objects.equals(status, that.status) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentType, orderId, amount, status, createdAt);
    }
}
