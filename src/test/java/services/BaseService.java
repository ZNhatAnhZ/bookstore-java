package services;

import com.book.dto.JwtModel;
import com.book.model.UsersEntity;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class BaseService {
    protected UsersEntity testUsersEntity = new UsersEntity();
    protected JwtModel jwtModel = new JwtModel();
    protected String host;
    protected String loginRoute;
    protected String userGetUserRoute;
    protected Properties properties;

    public BaseService() throws IOException {
        properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/test.properties"));

        testUsersEntity.setUserName("testUser");
        testUsersEntity.setPassword("newPassword");

        host = properties.getProperty("host");
        loginRoute = properties.getProperty("loginRoute");
        userGetUserRoute = properties.getProperty("userGetUserRoute");

        if (!this.getClass().getSimpleName().equalsIgnoreCase("UserTest")) {
            getJwtToken();
            getUsersEntity();
        }
    }

    private void getJwtToken() {
        String url = host + loginRoute;

        Response response =  BaseAPIUtil.sendPostRequest(url, "", testUsersEntity, 200);

        if (response.getStatus() != 200) {
            log.error("no jwtToken");
        }
        jwtModel = response.readEntity(JwtModel.class);
    }

    private void getUsersEntity() {
        String url = host + userGetUserRoute;

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), new MultivaluedStringMap(), 200);

        if (response.getStatus() != 200) {
            log.error("no user");
        }
        testUsersEntity = response.readEntity(UsersEntity.class);
    }
}
