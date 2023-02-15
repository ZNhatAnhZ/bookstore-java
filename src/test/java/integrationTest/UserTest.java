package integrationTest;

import com.book.dto.JwtModel;
import com.book.dto.UserDTO;
import com.book.model.UsersEntity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.TestUtil;

import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class UserTest {
    private Client client = ClientBuilder.newClient();
    private JwtModel jwtModel = new JwtModel();
    private String host;
    private String registerRoute;
    private String loginRoute;
    private String userChangePasswordRoute;
    private String userGetUserRoute;
    private String testCSVFile;

    @BeforeClass
    public void setUp() throws IOException {
        Resource resource = new ClassPathResource("/test.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);

        host = properties.getProperty("host");
        registerRoute = properties.getProperty("registerRoute");
        loginRoute = properties.getProperty("loginRoute");
        userChangePasswordRoute = properties.getProperty("userChangePasswordRoute");
        userGetUserRoute = properties.getProperty("userGetUserRoute");
        testCSVFile = "usertest.csv";
    }

    @Test(dataProvider = "getCSVDataForRegistration")
    public void sendRegistrationRequest(String testCaseId) {
        String url = host + registerRoute;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUserName(prop.getProperty("userName"));
        usersEntity.setPassword(prop.getProperty("password"));

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(usersEntity, MediaType.APPLICATION_JSON));

        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"sendRegistrationRequest"}, dataProvider = "getCSVDataForJwtToken")
    public void getJwtToken(String testCaseId) {
        String url = host + loginRoute;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUserName(prop.getProperty("userName"));
        usersEntity.setPassword(prop.getProperty("password"));

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(usersEntity, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("getCSVDataForJwtTokenSuccess")) {
            jwtModel = response.readEntity(JwtModel.class);
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"getJwtToken"}, dataProvider = "getCSVDataForChangePassword")
    public void sendChangePasswordRequest(String testCaseId) {
        String url = host + userChangePasswordRoute;
        Properties prop = TestUtil.getCSVData(testCSVFile, testCaseId);

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(prop.getProperty("password"));
        userDTO.setNewPassword(prop.getProperty("newPassword"));

        Response response = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtModel.getJwt())
                .post(Entity.entity(userDTO, MediaType.APPLICATION_JSON));

        if (testCaseId.equalsIgnoreCase("getCSVDataForChangePasswordSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"sendRegistrationRequest"})
    public void getUserRequest() {
        String url = host + userGetUserRoute;

        Response successResponse = client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtModel.getJwt())
                .get();

        assertEquals(successResponse.getStatus(), 200);
    }

    @DataProvider(name = "getCSVDataForRegistration")
    public Object[][] getCSVDataForRegistration() {
        return new Object[][]{{"sendRegistrationRequest"}};
    }

    @DataProvider(name = "getCSVDataForJwtToken")
    public Object[][] getCSVDataForJwtToken() {
        return new Object[][]{{"getCSVDataForJwtTokenSuccess"}, {"getCSVDataForJwtTokenFail"}};
    }

    @DataProvider(name = "getCSVDataForChangePassword")
    public Object[][] getCSVDataForChangePassword() {
        return new Object[][]{{"getCSVDataForChangePasswordSuccess"}, {"getCSVDataForChangePasswordFail"}};
    }
}
