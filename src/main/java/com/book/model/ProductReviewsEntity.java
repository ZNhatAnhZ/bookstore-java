package com.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "product_reviews", schema = "e-commerce")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class ProductReviewsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "review_product_id")
    private Integer productId;
    @OneToOne
    @JoinColumn(name = "review_by")
    private UsersEntity usersEntity;
    @Basic
    @Column(name = "rating")
    private Integer rating;
    @Basic
    @Column(name = "comment")
    private String comment;
    @Basic
    @Column(name = "review_date")
    private Date reviewDate;
}
