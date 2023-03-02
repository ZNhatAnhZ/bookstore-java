package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.dto.ProductsDTO;
import com.book.model.ProductsEntity;
import factory.ModelFactory;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.ProductService;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class CreateProductTest extends BaseTest{
    private ProductService productService;
    private String getProductByName;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private NewProductDTO newProductDTO = new NewProductDTO();

    @BeforeClass
    public void setUp() throws IOException {
        productService = new ProductService();
        getProductByName = properties.getProperty("getProductByName");
        testCSVFile = "productTest.csv";
    }

    @Test(dataProvider = "createProductCSVData")
    public void createProduct(String testCaseId) {
        Properties prop = loadCSVData(testCaseId);

        newProductDTO = ModelFactory.getNewProductDTO(prop);

        productService.createProduct(newProductDTO);
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
        productsEntityList = productService.getAllProductOfTestUser();
        assertTrue(verifyProductExistInAList(productsEntityList, newProductDTO));
    }

    @AfterClass
    public void tearDown() {
        for(ProductsEntity product : productsEntityList) {
            NewProductDTO newProductDTO1 = new NewProductDTO();
            newProductDTO1.setProductId(product.getId());
            productService.deleteProduct(newProductDTO1);
        }
    }

    private Boolean verifyProductExistInAList(List<ProductsEntity> productsEntityList, NewProductDTO newProductDTO) {
        for(ProductsEntity products : productsEntityList) {
            if (newProductDTO.equals(products)) {
                return true;
            }
        }
        return false;
    }

    @DataProvider(name = "createProductCSVData")
    public Object[][] createProductCSVData() {
        return new Object[][]{{"createProduct"}};
    }
}
