package factory;

import com.book.dto.CartItemsDTO;
import com.book.dto.NewProductDTO;
import com.book.dto.ProductReviewDTO;
import com.book.dto.UserDTO;

import java.util.Properties;

public class ModelFactory {
    public static NewProductDTO getNewProductDTO(Properties prop) {
        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));
        return newProductDTO;
    }

    public static UserDTO getUserDTO(Properties prop) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(prop.getProperty("password"));
        userDTO.setNewPassword(prop.getProperty("newPassword"));
        return userDTO;
    }

    public static ProductReviewDTO getProductReviewDTO(Properties prop) {
        return new ProductReviewDTO(prop.getProperty("comment"), Integer.parseInt(prop.getProperty("rating")), Integer.parseInt(prop.getProperty("productId")));
    }

    public static CartItemsDTO getCartItemsDTO(Properties prop) {
        return new CartItemsDTO(Integer.parseInt(prop.getProperty("productId")), Integer.parseInt(prop.getProperty("quantity")));
    }
}
