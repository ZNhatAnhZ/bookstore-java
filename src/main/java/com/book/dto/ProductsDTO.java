package com.book.dto;

import com.book.model.ProductsEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductsDTO {
    private List<ProductsEntity> productsEntityList;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}
