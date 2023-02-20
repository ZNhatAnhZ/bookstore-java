package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNotEquals;

public class DeleteProductTest extends BaseTest{
    private String getProductByIdRoute;
    private String getProductsByProviderId;
    private String deleteProduct;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();

    @BeforeClass
    public void setUp() {
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
        deleteProduct = properties.getProperty("deleteProduct");
        testCSVFile = "productTest.csv";
    }

    @Test
    public void getProductsByProviderId() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        productsEntityList = response.readEntity(new GenericType<List<ProductsEntity>>() {});
    }

    @Test(dependsOnMethods = {"getProductsByProviderId"})
    public void deleteProduct() {
        String url = host + deleteProduct;

        NewProductDTO newProductDTO = new NewProductDTO();
        for(ProductsEntity products : productsEntityList) {
            newProductDTO.setProductId(products.getId());
            Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
        }
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductsByProviderId() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        assertTrue(response.readEntity(new GenericType<List<ProductsEntity>>() {}).isEmpty());
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductById() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        for(ProductsEntity products : productsEntityList) {
            multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(products.getId())));

            BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 403);
        }
    }

    @Test(dependsOnMethods = {"getEmptyProductsByProviderId"})
    public void deleteProductEmpty() {
        String url = host + deleteProduct;
        NewProductDTO newProductDTO = new NewProductDTO();
        for(ProductsEntity products : productsEntityList) {
            newProductDTO.setProductId(products.getId());
            BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 403);
        }
    }
}
