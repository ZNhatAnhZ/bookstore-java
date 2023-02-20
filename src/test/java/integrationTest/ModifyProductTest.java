package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class ModifyProductTest extends BaseTest{
    private String getProductByIdRoute;
    private String getProductsByProviderId;
    private String modifyProduct;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private NewProductDTO newProductDTO = new NewProductDTO();

    @BeforeClass
    public void setUp() {
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
        modifyProduct = properties.getProperty("modifyProduct");
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

    @Test(dataProvider = "modifyProductCSVData", dependsOnMethods = {"getProductsByProviderId"})
    public void modifyProduct(String testCaseId) {
        String url = host + modifyProduct;
        Properties prop = loadCSVData(testCaseId);

        newProductDTO = new NewProductDTO();
        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));

        for(ProductsEntity products : productsEntityList) {
            newProductDTO.setProductId(products.getId());
            Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
        }
    }

    @Test(dependsOnMethods = {"modifyProduct"})
    public void getProductByIdAfterModified() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        for(ProductsEntity products : productsEntityList) {
            multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(products.getId())));

            Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
            assertTrue(verifyProductInfo(response.readEntity(ProductsEntity.class), newProductDTO));
        }
    }

    private Boolean verifyProductInfo(ProductsEntity productsEntity, NewProductDTO newProductDTO) {
        NewProductDTO temp = new NewProductDTO();

        newProductDTO.setProductId(null);
        temp.setCategoryName(productsEntity.getCategoryEntity().getCategoryName());
        temp.setProductDetails(productsEntity.getProductDetails());
        temp.setProductName(productsEntity.getProductName());
        temp.setProductPrice(productsEntity.getProductPrice());
        temp.setQuantity(productsEntity.getQuantity());
        temp.setProductPhoto(productsEntity.getProductPhoto());

        return temp.equals(newProductDTO);
    }
    @DataProvider(name = "modifyProductCSVData")
    public Object[][] modifyProductCSVData() {
        return new Object[][]{{"modifyProductSuccess"}};
    }
}
