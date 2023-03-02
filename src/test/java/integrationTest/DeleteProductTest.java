package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.model.ProductsEntity;
import factory.ModelFactory;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.ProductService;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class DeleteProductTest extends BaseTest{
    private ProductService productService;
    private String getProductByIdRoute;
    private String deleteProduct;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();

    @BeforeClass
    public void setUp1() throws IOException {
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        deleteProduct = properties.getProperty("deleteProduct");
        testCSVFile = "productTest.csv";
        productService = new ProductService();
    }

    @BeforeClass(dependsOnMethods = {"setUp1"})
    public void setUp2() {
        Properties prop = loadCSVProductData();
        productService.createProduct(ModelFactory.getNewProductDTO(prop));

        productsEntityList = productService.getAllProductOfTestUser();
    }

    @Test
    public void deleteProduct() {
        NewProductDTO newProductDTO = new NewProductDTO();
        for(ProductsEntity products : productsEntityList) {
            newProductDTO.setProductId(products.getId());
            productService.deleteProduct(newProductDTO);
        }
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductsByProviderId() {
        List<ProductsEntity> productsEntities = productService.getAllProductOfTestUser();
        assertTrue(productsEntities.isEmpty());
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
