package integrationTest;

import com.book.dto.JwtModel;
import com.book.dto.UserDTO;
import com.book.model.UsersEntity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;
import util.TestUtil;

import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class UserTest extends BaseTest {
    private String registerRoute;
    private String userChangePasswordRoute;

    @BeforeClass
    public void setUp() {
        registerRoute = properties.getProperty("registerRoute");
        userChangePasswordRoute = properties.getProperty("userChangePasswordRoute");
        testCSVFile = "usertest.csv";
    }

    @Test(dataProvider = "getCSVDataForRegistration")
    public void sendRegistrationRequest(String testCaseId) {
        String url = host + registerRoute;
        Properties prop = loadCSVData(testCaseId);

        testUsersEntity.setUserName(prop.getProperty("userName"));
        testUsersEntity.setPassword(prop.getProperty("password"));

        Response response =  BaseAPIUtil.sendPostRequest(url, "", testUsersEntity);

        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"sendRegistrationRequest"}, dataProvider = "getCSVDataForJwtToken")
    public void getJwtToken(String testCaseId) {
        String url = host + loginRoute;
        Properties prop = loadCSVData(testCaseId);

        testUsersEntity.setPassword(prop.getProperty("password"));

        Response response =  BaseAPIUtil.sendPostRequest(url, "", testUsersEntity);

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
        Properties prop = loadCSVData(testCaseId);

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(prop.getProperty("password"));
        userDTO.setNewPassword(prop.getProperty("newPassword"));

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), userDTO);

        if (testCaseId.equalsIgnoreCase("getCSVDataForChangePasswordSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"sendRegistrationRequest"})
    public void getUserRequest() {
        String url = host + userGetUserRoute;

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), new MultivaluedStringMap());

        assertEquals(response.getStatus(), 200);
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
