package com.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category", schema = "e-commerce")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@NoArgsConstructor
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "category_name")
    private String categoryName;
}
