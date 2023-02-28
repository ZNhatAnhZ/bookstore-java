package integrationTest;

import com.book.dto.JwtModel;
import com.book.dto.NewProductDTO;
import com.book.model.UsersEntity;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import util.BaseAPIUtil;
import util.TestUtil;

import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

@Slf4j
public class BaseTest {
    protected UsersEntity testUsersEntity = new UsersEntity();
    protected JwtModel jwtModel = new JwtModel();
    protected String host;
    protected String loginRoute;
    protected String userGetUserRoute;
    protected String createProduct;
    protected String testCSVFile;
    protected Properties properties;

    @BeforeClass
    public void defaultSetUp() throws IOException {
        properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/test.properties"));

        testUsersEntity.setUserName("testUser");
        testUsersEntity.setPassword("newPassword");

        host = properties.getProperty("host");
        loginRoute = properties.getProperty("loginRoute");
        userGetUserRoute = properties.getProperty("userGetUserRoute");
        createProduct = properties.getProperty("createProduct");

        if (!this.getClass().getSimpleName().equalsIgnoreCase("UserTest")) {
            getJwtToken();
            getUsersEntity();
        }
    }

    private void getJwtToken() {
        String url = host + loginRoute;

        Response response =  BaseAPIUtil.sendPostRequest(url, "", testUsersEntity, 200);
        jwtModel = response.readEntity(JwtModel.class);

        if (response.getStatus() != 200) {
            log.error("no jwtToken");
            throw new SkipException("");
        }
    }

    private void getUsersEntity() {
        String url = host + userGetUserRoute;

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), new MultivaluedStringMap(), 200);
        testUsersEntity = response.readEntity(UsersEntity.class);

        if (response.getStatus() != 200) {
            log.error("no user");
            throw new SkipException("");
        }
    }

    public Properties loadCSVData(String testCaseId) {
        return TestUtil.getCSVData(testCSVFile, testCaseId);
    }

    public void CreateProduct() {
        String url = host + createProduct;
        Properties prop = TestUtil.getCSVData("productTest.csv", "createProductSuccess");

        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));

        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO, 200);
    }
}
