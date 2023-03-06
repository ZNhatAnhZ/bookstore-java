package integrationTest;

import com.book.dto.CartItemsDTO;
import com.book.dto.NewProductDTO;
import com.book.model.CartItemsEntity;
import com.book.model.ProductsEntity;
import factory.ModelFactory;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.TestNGAntTask;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.CartItemsService;
import services.ProductService;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class CartItemsTest extends BaseTest{
    private ProductService productService;
    private CartItemsService cartItemsService;
    private List<CartItemsEntity> cartItemsEntityList;
    private String deleteCartItemsRoute;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();

    @BeforeClass
    public void setUp1() throws IOException {
        deleteCartItemsRoute = properties.getProperty("deleteCartItemsRoute");
        testCSVFile = "cartItemTest.csv";
        productService = new ProductService();
        cartItemsService = new CartItemsService();
    }

    @BeforeClass(dependsOnMethods = {"setUp1"})
    public void setUp2() {
        Properties prop = loadCSVProductData();
        productService.createProduct(ModelFactory.getNewProductDTO(prop));

        productsEntityList = productService.getAllProductOfTestUser();
    }

    @Test(dataProvider = "getCSVDataAddCartItems")
    public void addCartItems(String testCaseId) {
        Properties prop = loadCSVData(testCaseId);

        for(ProductsEntity product : productsEntityList) {
            prop.setProperty("productId", String.valueOf(product.getId()));
            CartItemsDTO cartItemsDTO = ModelFactory.getCartItemsDTO(prop);
            cartItemsService.addCartItems(cartItemsDTO);
        }
    }

    @Test(dependsOnMethods = {"addCartItems"})
    public void getCartItems() {
        cartItemsEntityList = cartItemsService.getCartItems();
    }

    @Test(dependsOnMethods = {"getCartItems"})
    public void deleteCartItems() {
        Properties prop = new Properties();
        for(CartItemsEntity cartItemsEntity : cartItemsEntityList) {
            prop.setProperty("productId", String.valueOf(cartItemsEntity.getId()));
            prop.setProperty("quantity", String.valueOf(0));
            CartItemsDTO cartItemsDTO = ModelFactory.getCartItemsDTO(prop);
            cartItemsService.deleteCartItems(cartItemsDTO);
        }
    }

    @Test(dependsOnMethods = {"deleteCartItems"})
    public void getEmptyCartItems() {
        List<CartItemsEntity> result = cartItemsService.getCartItems();
        assertTrue(result.isEmpty());
    }

    @Test(dependsOnMethods = {"getEmptyCartItems"})
    public void deleteCartItemsWhenEmpty() {
        String url = host + deleteCartItemsRoute;
        Properties prop = new Properties();
        for(CartItemsEntity cartItemsEntity : cartItemsEntityList) {
            prop.setProperty("productId", String.valueOf(cartItemsEntity.getId()));
            prop.setProperty("quantity", String.valueOf(0));
            CartItemsDTO cartItemsDTO = ModelFactory.getCartItemsDTO(prop);
            BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO, 403);
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

    @DataProvider(name = "getCSVDataAddCartItems")
    public Object[][] getCSVDataAddCartItems() {
        return new Object[][]{{"getCSVDataAddCartItemsSuccess"}};
    }
}
