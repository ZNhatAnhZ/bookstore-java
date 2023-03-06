package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;
import com.book.model.ProductsEntity;
import factory.ModelFactory;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.ProductService;
import util.BaseAPIUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

@Slf4j
public class ProductReviewTest extends BaseTest{
    private ProductService productService;
    private String getProductReviewByProductIdRoute;
    private String getAverageRatingByProductIdRoute;
    private String addProductReviewRoute;
    private List<ProductReviewsEntity> productReviewsEntityList;
    private ProductReviewDTO productReviewDTO;
    private List<ProductReviewDTO> productReviewDTOList = new ArrayList<>();
    private ProductsEntity productsEntity;

    @BeforeClass
    public void setUp1() throws IOException {
        getProductReviewByProductIdRoute = properties.getProperty("getProductReviewByProductIdRoute");
        getAverageRatingByProductIdRoute = properties.getProperty("getAverageRatingByProductIdRoute");
        addProductReviewRoute = properties.getProperty("addProductReviewRoute");
        testCSVFile = "productReviewTest.csv";
        productService = new ProductService();
    }

    @BeforeClass(dependsOnMethods = {"setUp1"})
    public void setUp2() {
        Properties prop = loadCSVProductData();
        productService.createProduct(ModelFactory.getNewProductDTO(prop));

        productsEntity = productService.getAllProductOfTestUser().get(0);
    }

    @Test
    public void getProductReviewByProductId() {
        String url = host + getProductReviewByProductIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(String.valueOf(productsEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        productReviewsEntityList = response.readEntity(new GenericType<List<ProductReviewsEntity>>() {});
    }

    @Test(dependsOnMethods = {"getProductReviewByProductId"})
    public void getAverageRating() {
        String url = host + getAverageRatingByProductIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(String.valueOf(productsEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        assertEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
    }

    @Test(dataProvider = "getCSVDataAddProductReview", dependsOnMethods = {"getAverageRating"})
    public void addProductReview(String testCaseId) {
        String url = host+addProductReviewRoute;
        Properties prop = loadCSVData(testCaseId);
        prop.setProperty("productId", String.valueOf(productsEntity.getId()));

        productReviewDTO = ModelFactory.getProductReviewDTO(prop);
        BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), productReviewDTO, 200);
        productReviewDTOList.add(productReviewDTO);
    }

    @Test(dependsOnMethods = {"addProductReview"})
    public void getProductReviewByProductIdAfterAddProductReview() {
        String url = host + getProductReviewByProductIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(String.valueOf(productsEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        productReviewsEntityList = response.readEntity(new GenericType<List<ProductReviewsEntity>>() {});
        assertTrue(verifyProductReview(productReviewsEntityList, productReviewDTOList));
    }

    @Test(dependsOnMethods = {"getProductReviewByProductIdAfterAddProductReview"})
    public void getAverageRatingAfterAddProductReview() {
        String url = host + getAverageRatingByProductIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("productId", Collections.singletonList(String.valueOf(productsEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
        assertEquals(calculateAverageRatingOfAProduct(productReviewsEntityList), response.readEntity(Integer.class));
    }

    @AfterClass
    public void tearDown() {
        NewProductDTO newProductDTO1 = new NewProductDTO();
        newProductDTO1.setProductId(productsEntity.getId());
        productService.deleteProduct(newProductDTO1);
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

    private Boolean verifyProductReview(List<ProductReviewsEntity> productReviewsEntityList, List<ProductReviewDTO> productReviewDTOList) {
        for(ProductReviewsEntity productReviewsEntity : productReviewsEntityList) {
            for(ProductReviewDTO productReviewDTO : productReviewDTOList) {
                if (productReviewDTO.equals(productReviewsEntity)) {
                    return true;
                }
            }
        }
        return false;
    }

    @DataProvider(name = "getCSVDataAddProductReview")
    public Object[][] getCSVDataAddProductReview() {
        return new Object[][]{{"addProductReview1"}, {"addProductReview2"}, {"addProductReview3"}};
    }
}
