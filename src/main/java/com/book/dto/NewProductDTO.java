package com.book.dto;

import lombok.Data;

@Data
public class NewProductDTO extends JwtModel{
    private int productId;
    private String categoryName;
    private String productName;
    private int productPrice;
    private int quantity;
    private String productPhoto;
    private String productDetails;
}
