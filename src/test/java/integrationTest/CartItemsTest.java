package integrationTest;

import com.book.dto.CartItemsDTO;
import com.book.model.CartItemsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class CartItemsTest extends BaseTest{
    private CartItemsEntity cartItemsEntity;
    private String getCartItemsRoute;
    private String addCartItemsRoute;
    private String deleteCartItemsRoute;

    @BeforeClass
    public void setUp() {
        getCartItemsRoute = properties.getProperty("getCartItemsRoute");
        addCartItemsRoute = properties.getProperty("addCartItemsRoute");
        deleteCartItemsRoute = properties.getProperty("deleteCartItemsRoute");
        testCSVFile = "cartItemTest.csv";
    }

    @Test(dataProvider = "getCSVDataAddCartItems")
    public void addCartItems(String testCaseId) {
        String url = host + addCartItemsRoute;
        Properties prop = loadCSVData(testCaseId);
        CartItemsDTO cartItemsDTO = new CartItemsDTO(Integer.parseInt(prop.getProperty("productId")), Integer.parseInt(prop.getProperty("quantity")));

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO);

        if (testCaseId.equalsIgnoreCase("getCSVDataAddCartItemsSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"addCartItems"})
    public void getCartItems() {
        String url = host + getCartItemsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap);

        cartItemsEntity = response.readEntity(new GenericType<List<CartItemsEntity>>() {}).get(0);
        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"getCartItems"}, dataProvider = "getCSVDataDeleteCartItems")
    public void deleteCartItems(String testCaseId) {
        String url = host + deleteCartItemsRoute;
        Properties prop = loadCSVData(testCaseId);
        CartItemsDTO cartItemsDTO;

        if (testCaseId.equalsIgnoreCase("getCSVDataDeleteCartItemsSuccess")) {
            cartItemsDTO = new CartItemsDTO(cartItemsEntity.getId(), 0);
        } else {
            cartItemsDTO = new CartItemsDTO(Integer.parseInt(prop.getProperty("cartItemId")), 0);
        }

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO);

        if (testCaseId.equalsIgnoreCase("getCSVDataDeleteCartItemsSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"deleteCartItems"})
    public void getEmptyCartItems() {
        String url = host + getCartItemsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.putSingle("id", String.valueOf(testUsersEntity.getId()));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap);

        List<CartItemsEntity> result = response.readEntity(new GenericType<List<CartItemsEntity>>() {});
        assertEquals(response.getStatus(), 200);
        assertTrue(result.isEmpty());
    }

    @Test(dependsOnMethods = {"getEmptyCartItems"})
    public void deleteCartItemsWhenEmpty() {
        String url = host + deleteCartItemsRoute;
        CartItemsDTO cartItemsDTO = new CartItemsDTO(cartItemsEntity.getId(), 0);

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), cartItemsDTO);

        assertNotEquals(response.getStatus(), 200);
    }

    @DataProvider(name = "getCSVDataAddCartItems")
    public Object[][] getCSVDataAddCartItems() {
        return new Object[][]{{"getCSVDataAddCartItemsSuccess"}, {"getCSVDataAddCartItemsFail"}};
    }

    @DataProvider(name = "getCSVDataDeleteCartItems")
    public Object[][] getCSVDataDeleteCartItems() {
        return new Object[][]{{"getCSVDataDeleteCartItemsSuccess"}, {"getCSVDataDeleteCartItemsFail"}};
    }
}
