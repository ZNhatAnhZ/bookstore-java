package com.book.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "shipping", schema = "e-commerce", catalog = "")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

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
