package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import factory.ModelFactory;
import jakarta.ws.rs.core.Response;
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

public class ModifyProductTest extends BaseTest{
    private ProductService productService;
    private String getProductByIdRoute;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private NewProductDTO newProductDTO = new NewProductDTO();

    @BeforeClass
    public void setUp1() throws IOException {
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        testCSVFile = "productTest.csv";
        productService = new ProductService();
    }

    @BeforeClass(dependsOnMethods = {"setUp1"})
    public void setUp2() {
        Properties prop = loadCSVProductData();
        productService.createProduct(ModelFactory.getNewProductDTO(prop));

        productsEntityList = productService.getAllProductOfTestUser();
    }

    @Test(dataProvider = "modifyProductCSVData")
    public void modifyProduct(String testCaseId) {
        Properties prop = loadCSVData(testCaseId);
        newProductDTO = ModelFactory.getNewProductDTO(prop);

        for(ProductsEntity products : productsEntityList) {
            newProductDTO.setProductId(products.getId());
            productService.modifyProduct(newProductDTO);
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

    @AfterClass
    public void tearDown() {
        for(ProductsEntity product : productsEntityList) {
            NewProductDTO newProductDTO1 = new NewProductDTO();
            newProductDTO1.setProductId(product.getId());
            productService.deleteProduct(newProductDTO1);
        }
    }

    private Boolean verifyProductInfo(ProductsEntity productsEntity, NewProductDTO newProductDTO) {
        return newProductDTO.equals(productsEntity);
    }
    @DataProvider(name = "modifyProductCSVData")
    public Object[][] modifyProductCSVData() {
        return new Object[][]{{"modifyProductSuccess"}};
    }
}
