package integrationTest;

import com.book.dto.JwtModel;
import com.book.dto.NewProductDTO;
import com.book.dto.ProductsDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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
public class ProductTest {
    private Client client = ClientBuilder.newClient();
    private String host;
    private String getProductsRoute;
    private String getProductsByCategoryRoute;
    private String getProductByIdRoute;
    private String getProductByName;
    private String getProductsByProviderId;
    private String createProduct;
    private String modifyProduct;
    private String deleteProduct;
    private String testCSVFile = "productTest.csv";
    private CategoryEntity categoryEntity;
    private UsersEntity testUsersEntity = new UsersEntity();
    private JwtModel jwtModel;
    private String loginRoute;
    private ProductsEntity productsEntity;
    private String userGetUserRoute;

    @BeforeClass
    public void setUp() throws IOException {
        Resource resource = new ClassPathResource("/test.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);

        host = properties.getProperty("host");
        getProductsRoute = properties.getProperty("getProductsRoute");
        getProductsByCategoryRoute = properties.getProperty("getProductsByCategoryRoute");
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        getProductByName = properties.getProperty("getProductByName");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
        createProduct = properties.getProperty("createProduct");
        modifyProduct = properties.getProperty("modifyProduct");
        deleteProduct = properties.getProperty("deleteProduct");
        loginRoute = properties.getProperty("loginRoute");
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

    @DataProvider(name = "getAllProductsCSVData")
    public Object[][] getAllProductsCSVData() {
        return new Object[][]{{"getProductDefault"}, {"getProductNewest"}, {"getProductPrice_asc"}, {"getProductPrice_desc"}, {"getProductBest_seller"}};
    }
    @DataProvider(name = "getProductsByCategoryCSVData")
    public Object[][] getProductsByCategoryCSVData() {
        return new Object[][]{{"getProductsByCategoryFail"}, {"getProductsByCategorySuccess"}};
    }
    @DataProvider(name = "getProductByIdCSVData")
    public Object[][] getProductByIdCSVData() {
        return new Object[][]{{"getProductByIdFail"}, {"getProductByIdSuccess"}};
    }
    @DataProvider(name = "getProductByNameCSVData")
    public Object[][] getProductByNameCSVData() {
        return new Object[][]{{"getProductByName"}};
    }
    @DataProvider(name = "createProductCSVData")
    public Object[][] createProductCSVData() {
        return new Object[][]{{"createProductSuccess"}};
    }
    @DataProvider(name = "modifyProductCSVData")
    public Object[][] modifyProductCSVData() {
        return new Object[][]{{"modifyProductSuccess"}, {"modifyProductFail"}};
    }
    @DataProvider(name = "deleteProductCSVData")
    public Object[][] deleteProductCSVData() {
        return new Object[][]{{"deleteProductFail"}, {"deleteProductSuccess"}};
    }

    @Test(dataProvider = "getAllProductsCSVData")
    public void getAllProducts(String sort) {
        String url = host + getProductsRoute;

        Response response = client
                .target(url)
                .queryParam("sort", sort)
                .request(MediaType.APPLICATION_JSON)
                .get();

        categoryEntity = response.readEntity(ProductsDTO.class).getProductsEntityList().get(0).getCategoryEntity();
        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"getAllProducts"}, dataProvider = "getProductsByCategoryCSVData")
    public void getProductsByCategory(String testCaseId) {
        String url = host + getProductsByCategoryRoute;
        String categoryName1;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        if (testCaseId.equalsIgnoreCase("getProductsByCategoryFail")) {
            categoryName1 = prop.getProperty("categoryName");
        } else {
            categoryName1 = categoryEntity.getCategoryName();
        }

        Response response = client
                .target(url)
                .queryParam("categoryName", categoryName1)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (testCaseId.equalsIgnoreCase("getProductsByCategoryFail")) {
            assertNotEquals(response.getStatus(), 200);
        } else {
            assertEquals(response.getStatus(), 200);
        }
    }

    @Test(dataProvider = "getProductByIdCSVData")
    public void getProductById(String testCaseId) {
        String url = host + getProductByIdRoute;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        Response response = client
                .target(url)
                .queryParam("id", Integer.parseInt(prop.getProperty("productId")))
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (testCaseId.equalsIgnoreCase("getProductByIdSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }
    @Test(dataProvider = "getProductByNameCSVData")
    public void getProductByName(String testCaseId) {
        String url = host + getProductByName;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        Response response = client
                .target(url)
                .queryParam("productName", prop.getProperty("productName"))
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(response.getStatus(), 200);
    }

    @Test(dataProvider = "createProductCSVData", dependsOnMethods = {"getProductByName"})
    public void createProduct(String testCaseId) {
        String url = host + createProduct;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(newProductDTO, MediaType.APPLICATION_JSON));

        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"createProduct"})
    public void getProductsByProviderId() {
        String url = host + getProductsByProviderId;

        Response response = client
                .target(url)
                .queryParam("providerId", testUsersEntity.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .get();

        productsEntity = response.readEntity(new GenericType<List<ProductsEntity>>() {}).get(0);
        assertEquals(response.getStatus(), 200);
    }
    @Test(dataProvider = "modifyProductCSVData", dependsOnMethods = {"getProductsByProviderId"})
    public void modifyProduct(String testCaseId) {
        String url = host + modifyProduct;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("ProductDetails"));
        newProductDTO.setProductName(prop.getProperty("ProductName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("ProductPhoto"));

        if (testCaseId.equalsIgnoreCase("modifyProductSuccess")) {
            newProductDTO.setProductId(productsEntity.getId());
        } else {
            newProductDTO.setProductId(Integer.valueOf(prop.getProperty("productId")));
        }

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(newProductDTO, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("modifyProductSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }
    @Test(dataProvider = "deleteProductCSVData", dependsOnMethods = {"getProductsByProviderId", "modifyProduct"})
    public void deleteProduct(String testCaseId) {
        String url = host + deleteProduct;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        NewProductDTO newProductDTO = new NewProductDTO();

        if (testCaseId.equalsIgnoreCase("deleteProductFail")) {
            newProductDTO.setProductId(Integer.valueOf(prop.getProperty("productId")));
        } else {
            newProductDTO.setProductId(productsEntity.getId());
        }

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(newProductDTO, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("deleteProductFail")) {
            assertNotEquals(response.getStatus(), 200);
        } else {
            assertEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductsByProviderId() {
        String url = host + getProductsByProviderId;

        Response response = client
                .target(url)
                .queryParam("providerId", testUsersEntity.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .get();

        List<ProductsEntity> productsEntityList = response.readEntity(new GenericType<List<ProductsEntity>>() {});
        assertEquals(response.getStatus(), 200);
        assertTrue(productsEntityList.isEmpty());
    }

    @Test(dependsOnMethods = {"getEmptyProductsByProviderId"})
    public void deleteProductEmpty() {
        String url = host + deleteProduct;
        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setProductId(productsEntity.getId());

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(newProductDTO, MediaType.APPLICATION_JSON));

        assertNotEquals(response.getStatus(), 200);
    }
}
