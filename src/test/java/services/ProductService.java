package services;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ProductService extends BaseService{
    private String getProductsByProviderId;
    private String createProduct;
    private String modifyProduct;
    private String deleteProduct;
    public ProductService() throws IOException {
        createProduct = properties.getProperty("createProduct");
        modifyProduct = properties.getProperty("modifyProduct");
        deleteProduct = properties.getProperty("deleteProduct");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
    }
    public List<ProductsEntity> getAllProductOfTestUser() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        return response.readEntity(new GenericType<List<ProductsEntity>>() {});
    }
    public void createProduct(NewProductDTO newProductDTO) {
        String url = host + createProduct;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
    }

    public void modifyProduct(NewProductDTO newProductDTO) {
        String url = host + modifyProduct;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
    }

    public void deleteProduct(NewProductDTO newProductDTO) {
        String url = host + deleteProduct;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
    }
}
