package com.book.dto;

import com.book.model.ProductsEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class NewProductDTO extends JwtModel{
    private Integer productId;
    private String categoryName;
    private String productName;
    private Integer productPrice;
    private Integer quantity;
    private String productPhoto;
    private String productDetails;

    public boolean equals(ProductsEntity product) {
        if (!Objects.equals(categoryName, product.getCategoryEntity().getCategoryName())) return false;
        if (!Objects.equals(productDetails, product.getProductDetails())) return false;
        if (!Objects.equals(productName, product.getProductName())) return false;
        if (!Objects.equals(productPrice, product.getProductPrice())) return false;
        if (!Objects.equals(quantity, product.getQuantity())) return false;
        return Objects.equals(productPhoto, product.getProductPhoto());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewProductDTO that)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(categoryName, that.categoryName)) return false;
        if (!Objects.equals(productName, that.productName)) return false;
        if (!Objects.equals(productPrice, that.productPrice)) return false;
        if (!Objects.equals(quantity, that.quantity)) return false;
        if (!Objects.equals(productPhoto, that.productPhoto)) return false;
        return Objects.equals(productDetails, that.productDetails);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (productPhoto != null ? productPhoto.hashCode() : 0);
        result = 31 * result + (productDetails != null ? productDetails.hashCode() : 0);
        return result;
    }
}
