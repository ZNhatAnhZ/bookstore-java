package integrationTest;

import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class CategoryTest extends BaseTest{
    private String getAllCategoryRoute;

    @BeforeClass
    public void setUp() throws IOException {
        getAllCategoryRoute = properties.getProperty("getAllCategoryRoute");
    }

    @Test
    public void getAllCategory() {
        String url = host+getAllCategoryRoute;

        Response response =  BaseAPIUtil.sendGetRequest(url, "", new MultivaluedStringMap());

        assertEquals(response.getStatus(), 200);
    }
}
