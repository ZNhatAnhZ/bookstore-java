package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.dto.ProductsDTO;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class CreateProductTest extends BaseTest{
    private String getProductByName;
    private String getProductsByProviderId;
    private String createProduct;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private NewProductDTO newProductDTO = new NewProductDTO();

    @BeforeClass
    public void setUp() {
        getProductByName = properties.getProperty("getProductByName");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
        createProduct = properties.getProperty("createProduct");
        testCSVFile = "productTest.csv";
    }

    @Test(dataProvider = "createProductCSVData")
    public void createProduct(String testCaseId) {
        String url = host + createProduct;
        Properties prop = loadCSVData(testCaseId);

        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));

        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
    }

    @Test(dependsOnMethods = {"createProduct"})
    public void getProductByName() {
        String url = host + getProductByName;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productName", Collections.singletonList(newProductDTO.getProductName()));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        productsEntityList = response.readEntity(ProductsDTO.class).getProductsEntityList();
        assertTrue(verifyProductExistInAList(productsEntityList, newProductDTO));
    }

    @Test(dependsOnMethods = {"getProductByName"})
    public void getProductsByProviderId() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        productsEntityList = response.readEntity(new GenericType<List<ProductsEntity>>() {});
        assertTrue(verifyProductExistInAList(productsEntityList, newProductDTO));
    }

    private Boolean verifyProductExistInAList(List<ProductsEntity> productsEntityList, NewProductDTO newProductDTO) {
        for(ProductsEntity products : productsEntityList) {
            NewProductDTO temp = new NewProductDTO();
            newProductDTO.setProductId(null);
            temp.setCategoryName(products.getCategoryEntity().getCategoryName());
            temp.setProductDetails(products.getProductDetails());
            temp.setProductName(products.getProductName());
            temp.setProductPrice(products.getProductPrice());
            temp.setQuantity(products.getQuantity());
            temp.setProductPhoto(products.getProductPhoto());

            if (temp.equals(newProductDTO)) {
                return true;
            }
        }
        return false;
    }

    @DataProvider(name = "createProductCSVData")
    public Object[][] createProductCSVData() {
        return new Object[][]{{"createProductSuccess"}};
    }
}
