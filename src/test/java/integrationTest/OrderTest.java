package integrationTest;

import com.book.dto.CartItemsDTO;
import com.book.dto.OrdersDTO;
import com.book.model.CartItemsEntity;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;
import util.TestUtil;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class OrderTest extends BaseTest{
    private String createOrderByCartRoute;
    private String createOrderByIdRoute;
    private String addCartItemsRoute;
    private String getCartItemsRoute;
    private String testCSVFileCartItem;
    private String getProductByIdRoute;
    private CartItemsDTO cartItemsDTO;
    private ProductsEntity productsEntity;
    private OrdersDTO ordersDTO;

    @BeforeClass
    public void setUp() {
        createOrderByCartRoute = properties.getProperty("createOrderByCartRoute");
        createOrderByIdRoute = properties.getProperty("createOrderByIdRoute");
        addCartItemsRoute = properties.getProperty("addCartItemsRoute");
        getCartItemsRoute = properties.getProperty("getCartItemsRoute");
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        testCSVFile = "orderTest.csv";
        testCSVFileCartItem = "cartItemTest.csv";
    }

    @Test
    public void createOrderByCartEmpty() {
        String url = host + createOrderByCartRoute;

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), String.class, 403);
    }

    @Test(dataProvider = "getCSVDataAddCartItems", dependsOnMethods = {"createOrderByCartEmpty"})
    public void addCartItems(String testCaseId) {
        String url = host + addCartItemsRoute;
        Properties prop = TestUtil.getCSVData(testCSVFileCartItem, testCaseId);
        cartItemsDTO = new CartItemsDTO(Integer.parseInt(prop.getProperty("productId")), Integer.parseInt(prop.getProperty("quantity")));

        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO, 200);
    }

    @Test(dependsOnMethods = {"addCartItems"})
    public void getProductById() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(cartItemsDTO.getProductId())));

        Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        productsEntity = response.readEntity(ProductsEntity.class);
    }

    @Test(dependsOnMethods = {"getProductById"})
    public void createOrderByCart() {
        String url = host + createOrderByCartRoute;
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), String.class, 200);
    }

    @Test(dependsOnMethods = {"createOrderByCart"})
    public void getProductByIdAfterCreatedOrder() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(cartItemsDTO.getProductId())));

        Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);

        ProductsEntity newProductsEntity = response.readEntity(ProductsEntity.class);
        assertEquals(productsEntity.getQuantity()-newProductsEntity.getQuantity(), cartItemsDTO.getQuantity());
        productsEntity = newProductsEntity;
    }

    @Test(dependsOnMethods = {"getProductByIdAfterCreatedOrder"})
    public void getEmptyCartItems() {
        String url = host + getCartItemsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap, 200);
        List<CartItemsEntity> result = response.readEntity(new GenericType<List<CartItemsEntity>>() {});
        assertTrue(result.isEmpty());
    }

    @Test(dataProvider = "getCSVDataCreateOrderById", dependsOnMethods = {"getEmptyCartItems"})
    public void createOrderById(String testCaseId) {
        String url = host + createOrderByIdRoute;
        Properties prop = loadCSVData(testCaseId);

        ordersDTO = new OrdersDTO(Integer.parseInt(prop.getProperty("productId")), Integer.parseInt(prop.getProperty("quantity")));

        if (testCaseId.equalsIgnoreCase("getCSVDataCreateOrderByIdSuccess")) {
            BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), ordersDTO, 200);
        } else {
            BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), ordersDTO, 403);
        }
    }

    @Test(dependsOnMethods = {"createOrderById"})
    public void getProductByIdAfterCreatedOrderByProductId() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(ordersDTO.getProductId())));

        Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);

        ProductsEntity newProductsEntity = response.readEntity(ProductsEntity.class);
        assertEquals(productsEntity.getQuantity()-newProductsEntity.getQuantity(), ordersDTO.getQuantity());
    }

    @DataProvider(name = "getCSVDataAddCartItems")
    public Object[][] getCSVDataAddCartItems() {
        return new Object[][]{{"getCSVDataAddCartItemsSuccess"}};
    }
    @DataProvider(name = "getCSVDataCreateOrderById")
    public Object[][] getCSVDataCreateOrderById() {
        return new Object[][]{{"getCSVDataCreateOrderByIdFail"}, {"getCSVDataCreateOrderByIdSuccess"}};
    }
}
