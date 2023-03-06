package com.book.dto;

import com.book.model.ProductReviewsEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class ProductReviewDTO extends JwtModel{
    private final String comment;
    private final Integer rating;
    private final Integer productId;

    public boolean equals(ProductReviewsEntity productReviewsEntity) {
        if (!Objects.equals(comment, productReviewsEntity.getComment())) return false;
        if (!Objects.equals(rating, productReviewsEntity.getRating())) return false;
        return Objects.equals(productId, productReviewsEntity.getProductId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductReviewDTO that)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(comment, that.comment)) return false;
        if (!Objects.equals(rating, that.rating)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }
}
