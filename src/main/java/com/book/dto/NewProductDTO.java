package com.book.dto;

import lombok.Data;

@Data
public class NewProductDTO extends JwtModel{
    private Integer productId;
    private String categoryName;
    private String productName;
    private Integer productPrice;
    private Integer quantity;
    private String productPhoto;
    private String productDetails;
}
