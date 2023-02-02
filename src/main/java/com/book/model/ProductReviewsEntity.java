package com.book.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "product_reviews", schema = "e-commerce", catalog = "")
public class ProductReviewsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "review_product_id")
    private Integer reviewProductId;
    @Basic
    @Column(name = "review_by")
    private Integer reviewBy;
    @Basic
    @Column(name = "rating")
    private Integer rating;
    @Basic
    @Column(name = "comment")
    private String comment;
    @Basic
    @Column(name = "review_date")
    private Date reviewDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReviewProductId() {
        return reviewProductId;
    }

    public void setReviewProductId(Integer reviewProductId) {
        this.reviewProductId = reviewProductId;
    }

    public Integer getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(Integer reviewBy) {
        this.reviewBy = reviewBy;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewsEntity that = (ProductReviewsEntity) o;
        return id == that.id && Objects.equals(reviewProductId, that.reviewProductId) && Objects.equals(reviewBy, that.reviewBy) && Objects.equals(rating, that.rating) && Objects.equals(comment, that.comment) && Objects.equals(reviewDate, that.reviewDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewProductId, reviewBy, rating, comment, reviewDate);
    }
}
