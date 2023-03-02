package services;

import com.book.dto.CartItemsDTO;
import com.book.model.CartItemsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CartItemsService extends BaseService{
    private String getCartItemsRoute;
    private String addCartItemsRoute;
    private String deleteCartItemsRoute;

    public CartItemsService() throws IOException {
        getCartItemsRoute = properties.getProperty("getCartItemsRoute");
        addCartItemsRoute = properties.getProperty("addCartItemsRoute");
        deleteCartItemsRoute = properties.getProperty("deleteCartItemsRoute");
    }

    public List<CartItemsEntity> getCartItems() {
        String url = host + getCartItemsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        return response.readEntity(new GenericType<List<CartItemsEntity>>() {});
    }

    public void addCartItems(CartItemsDTO cartItemsDTO) {
        String url = host + addCartItemsRoute;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO, 200);
    }

    public void deleteCartItems(CartItemsDTO cartItemsDTO) {
        String url = host + deleteCartItemsRoute;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO, 200);
    }
}
