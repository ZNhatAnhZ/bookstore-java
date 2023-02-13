package com.book.dto;

import com.book.model.ProductsEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductsDTO {
    private List<ProductsEntity> productsEntityList;
    private Integer currentPage;
    private long totalItems;
    private Integer totalPages;
}
