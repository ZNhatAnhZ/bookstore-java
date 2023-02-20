package integrationTest;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.io.IOException;

public class CategoryTest extends BaseTest{
    private String getAllCategoryRoute;

    @BeforeClass
    public void setUp() throws IOException {
        getAllCategoryRoute = properties.getProperty("getAllCategoryRoute");
    }

    @Test
    public void getAllCategory() {
        String url = host+getAllCategoryRoute;
        BaseAPIUtil.sendGetRequest(url, "", new MultivaluedStringMap(), 200);
    }
}
