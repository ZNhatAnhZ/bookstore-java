package integrationTest;

import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

@Slf4j
public class ProductReviewTest extends BaseTest{
    private String getProductReviewByProductIdRoute;
    private String getAverageRatingByProductIdRoute;
    private String addProductReviewRoute;
    private List<ProductReviewsEntity> productReviewsEntityList;
    private ProductReviewDTO productReviewDTO;

    @BeforeClass
    public void setUp() {
        getProductReviewByProductIdRoute = properties.getProperty("getProductReviewByProductIdRoute");
        getAverageRatingByProductIdRoute = properties.getProperty("getAverageRatingByProductIdRoute");
        addProductReviewRoute = properties.getProperty("addProductReviewRoute");
        testCSVFile = "productReviewTest.csv";
    }

    @Test(dataProvider = "getCSVDataGetProductReviewByProductId")
    public void getProductReviewByProductId(String testCaseId) {
        String url = host + getProductReviewByProductIdRoute;
        Properties prop = loadCSVData(testCaseId);
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(prop.getProperty("productId")));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);
        productReviewsEntityList = response.readEntity(new GenericType<List<ProductReviewsEntity>>() {});
    }

    @Test(dataProvider = "getCSVDataGetAverageRating", dependsOnMethods = {"getProductReviewByProductId"})
    public void getAverageRating(String testCaseId) {
        String url = host + getAverageRatingByProductIdRoute;
        Properties prop = loadCSVData(testCaseId);
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(prop.getProperty("productId")));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);
        if (testCaseId.equalsIgnoreCase("getAverageRatingSuccess")) {
            assertEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
        } else {
            assertNotEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
        }
    }

    @Test(dataProvider = "getCSVDataAddProductReview", dependsOnMethods = {"getAverageRating"})
    public void addProductReview(String testCaseId) {
        String url = host+addProductReviewRoute;
        Properties prop = loadCSVData(testCaseId);

        productReviewDTO = new ProductReviewDTO(prop.getProperty("comment"), Integer.parseInt(prop.getProperty("rating")), Integer.parseInt(prop.getProperty("productId")));

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), productReviewDTO);

        if (testCaseId.equalsIgnoreCase("addProductReviewSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dataProvider = "getCSVDataGetProductReviewByProductId", dependsOnMethods = {"addProductReview"})
    public void getProductReviewByProductIdAfterAddProductReview(String testCaseId) {
        String url = host + getProductReviewByProductIdRoute;
        Properties prop = loadCSVData(testCaseId);
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(prop.getProperty("productId")));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);
        productReviewsEntityList = response.readEntity(new GenericType<List<ProductReviewsEntity>>() {});
        assertTrue(verifyProductReview(productReviewsEntityList, productReviewDTO));
    }

    @Test(dataProvider = "getCSVDataGetAverageRating", dependsOnMethods = {"getProductReviewByProductIdAfterAddProductReview"})
    public void getAverageRatingAfterAddProductReview(String testCaseId) {
        String url = host + getAverageRatingByProductIdRoute;
        Properties prop = loadCSVData(testCaseId);
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(prop.getProperty("productId")));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);
        if (testCaseId.equalsIgnoreCase("getAverageRatingSuccess")) {
            assertEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
        } else {
            assertNotEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
        }
    }

    private int calculateAverageRatingOfAProduct(List<ProductReviewsEntity> productReviewsEntityList) {
        if (!productReviewsEntityList.isEmpty()) {
            AtomicInteger result = new AtomicInteger();
            productReviewsEntityList.forEach(e -> result.addAndGet(e.getRating()));
            result.getAndUpdate(v -> v / productReviewsEntityList.size());
            return result.get();
        } else {
            return 0;
        }
    }

    private Boolean verifyProductReview(List<ProductReviewsEntity> productReviewsEntityList, ProductReviewDTO productReviewDTO) {
        for(ProductReviewsEntity productReviewsEntity : productReviewsEntityList) {
            ProductReviewDTO temp = new ProductReviewDTO(productReviewsEntity.getComment(), productReviewsEntity.getRating(), productReviewsEntity.getProductId());

            if (temp.equals(productReviewDTO)) {
                return true;
            }
        }
        return false;
    }

    @DataProvider(name = "getCSVDataGetProductReviewByProductId")
    public Object[][] getCSVDataGetProductReviewByProductId() {
        return new Object[][]{{"getProductReviewByProductIdSuccess"}};
    }

    @DataProvider(name = "getCSVDataGetAverageRating")
    public Object[][] getCSVDataGetAverageRating() {
        return new Object[][]{{"getAverageRatingSuccess"}, {"getAverageRatingFail"}};
    }

    @DataProvider(name = "getCSVDataAddProductReview")
    public Object[][] getCSVDataAddProductReview() {
        return new Object[][]{{"addProductReviewFail"}, {"addProductReviewSuccess"}};
    }
}
