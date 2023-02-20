package integrationTest;

import com.book.dto.CartItemsDTO;
import com.book.dto.JwtModel;
import com.book.model.CartItemsEntity;
import com.book.model.UsersEntity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.TestUtil;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class CartItemsTest {
    private Client client = ClientBuilder.newClient();
    private UsersEntity testUsersEntity = new UsersEntity();
    private JwtModel jwtModel = new JwtModel();
    private CartItemsEntity cartItemsEntity;
    private String host;
    private String loginRoute;
    private String getCartItemsRoute;
    private String addCartItemsRoute;
    private CartItemsDTO cartItemsDTO;
    private String deleteCartItemsRoute;
    private String userGetUserRoute;
    private String testCSVFile = "cartItemTest.csv";

    @BeforeClass
    public void setUp() throws IOException {
        Resource resource = new ClassPathResource("/test.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);

        host = properties.getProperty("host");
        loginRoute = properties.getProperty("loginRoute");
        getCartItemsRoute = properties.getProperty("getCartItemsRoute");
        addCartItemsRoute = properties.getProperty("addCartItemsRoute");
        deleteCartItemsRoute = properties.getProperty("deleteCartItemsRoute");
        userGetUserRoute = properties.getProperty("userGetUserRoute");

        getJwtToken();
        getUsersEntity();
    }

    public void getJwtToken() {
        String url = host + loginRoute;
        testUsersEntity.setUserName("testUser");
        testUsersEntity.setPassword("newPassword");

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(testUsersEntity, MediaType.APPLICATION_JSON));
        jwtModel = response.readEntity(JwtModel.class);

        if (response.getStatus() != 200) {
            log.error("no jwtToken");
            throw new SkipException("");
        }
    }

    public void getUsersEntity() {
        String url = host + userGetUserRoute;

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .get();

        testUsersEntity = response.readEntity(UsersEntity.class);

        if (response.getStatus() != 200) {
            log.error("no user");
            throw new SkipException("");
        }
    }

    @Test(dataProvider = "getCSVDataAddCartItems")
    public void addCartItems(String testCaseId) {
        String url = host + addCartItemsRoute;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);
        cartItemsDTO = new CartItemsDTO(Integer.parseInt(prop.getProperty("productId")), Integer.parseInt(prop.getProperty("quantity")));

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(cartItemsDTO, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("getCSVDataAddCartItemsSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"addCartItems"})
    public void getCartItems() {
        String url = host + getCartItemsRoute;
        Response response = client
                .target(url)
                .queryParam("id", testUsersEntity.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .get();

        cartItemsEntity = response.readEntity(new GenericType<List<CartItemsEntity>>() {}).get(0);
        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"getCartItems"}, dataProvider = "getCSVDataDeleteCartItems")
    public void deleteCartItems(String testCaseId) {
        String url = host + deleteCartItemsRoute;
        CartItemsDTO cartItemsDTO;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        if (testCaseId.equalsIgnoreCase("getCSVDataDeleteCartItemsSuccess")) {
            cartItemsDTO = new CartItemsDTO(cartItemsEntity.getId(), 0);
        } else {
            cartItemsDTO = new CartItemsDTO(Integer.parseInt(prop.getProperty("cartItemId")), 0);
        }

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(cartItemsDTO, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("getCSVDataDeleteCartItemsSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"deleteCartItems"})
    public void getEmptyCartItems() {
        String url = host + getCartItemsRoute;
        Response response = client
                .target(url)
                .queryParam("id", testUsersEntity.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .get();

        List<CartItemsEntity> result = response.readEntity(new GenericType<List<CartItemsEntity>>() {});
        assertEquals(response.getStatus(), 200);
        assertTrue(result.isEmpty());
    }

    @Test(dependsOnMethods = {"getEmptyCartItems"}, dataProvider = "getCSVDataDeleteCartItemsWhenEmpty")
    public void deleteCartItemsWhenEmpty(String testCaseId) {
        String url = host + deleteCartItemsRoute;
        CartItemsDTO cartItemsDTO;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        cartItemsDTO = new CartItemsDTO(cartItemsEntity.getId(), 0);

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(cartItemsDTO, MediaType.APPLICATION_JSON));

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

    @DataProvider(name = "getCSVDataDeleteCartItemsWhenEmpty")
    public Object[][] getCSVDataDeleteCartItemsWhenEmpty() {
        return new Object[][]{{"getCSVDataDeleteCartItemsWhenEmpty"}};
    }
}
