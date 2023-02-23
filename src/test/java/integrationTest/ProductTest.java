package integrationTest;

import com.book.dto.NewProductDTO;
import com.book.dto.ProductsDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

@Slf4j
public class ProductTest extends BaseTest{
    private String getProductsRoute;
    private String getProductsByCategoryRoute;
    private String getProductByIdRoute;
    private String getProductByName;
    private String getProductsByProviderId;
    private String createProduct;
    private String modifyProduct;
    private String deleteProduct;
    private CategoryEntity categoryEntity;
    private ProductsEntity productsEntity;
    private int totalNumberOfProducts;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private NewProductDTO newProductDTO = new NewProductDTO();

    @BeforeClass
    public void setUp() {
        getProductsRoute = properties.getProperty("getProductsRoute");
        getProductsByCategoryRoute = properties.getProperty("getProductsByCategoryRoute");
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        getProductByName = properties.getProperty("getProductByName");
        getProductsByProviderId = properties.getProperty("getProductsByProviderId");
        createProduct = properties.getProperty("createProduct");
        modifyProduct = properties.getProperty("modifyProduct");
        deleteProduct = properties.getProperty("deleteProduct");
        testCSVFile = "productTest.csv";
    }

    @Test(dataProvider = "getAllProductsCSVData")
    public void getAllProducts(String sort) {
        String url = host + getProductsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("sort", Collections.singletonList(sort));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);

        ProductsDTO productsDTO = response.readEntity(ProductsDTO.class);
        categoryEntity = productsDTO.getProductsEntityList().get(0).getCategoryEntity();
        totalNumberOfProducts |= productsDTO.getTotalItems();

        if (productsEntityList.isEmpty()) {
            productsEntityList = productsDTO.getProductsEntityList();
        }

        assertEquals(totalNumberOfProducts, productsDTO.getTotalItems());
        assertTrue(verifySort(productsDTO, sort));
    }

    @Test(dependsOnMethods = {"getAllProducts"}, dataProvider = "getProductsByCategoryCSVData")
    public void getProductsByCategory(String testCaseId) {
        String url = host + getProductsByCategoryRoute;
        Properties prop = loadCSVData(testCaseId);
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        if (testCaseId.equalsIgnoreCase("getProductsByCategoryFail")) {
            multivaluedStringMap.put("categoryName", Collections.singletonList(prop.getProperty("categoryName")));
        } else {
            multivaluedStringMap.put("categoryName", Collections.singletonList(categoryEntity.getCategoryName()));
        }

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        if (testCaseId.equalsIgnoreCase("getProductsByCategoryFail")) {
            assertNotEquals(response.getStatus(), 200);
        } else {
            ProductsDTO productsDTO = response.readEntity(ProductsDTO.class);
            String categoryName = multivaluedStringMap.get("categoryName").get(0);

            assertEquals(response.getStatus(), 200);
            assertTrue(verifyCategory(productsDTO, categoryName));
        }
    }

    @Test(dependsOnMethods = {"getAllProducts"})
    public void getProductById() {
        String url = host + getProductByIdRoute;
        for(ProductsEntity product : productsEntityList) {
            MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

            multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(product.getId())));

            Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);
            assertEquals(response.getStatus(), 200);
            assertEquals(response.readEntity(ProductsEntity.class).getId(), product.getId());
        }
    }

    @Test(dataProvider = "createProductCSVData", dependsOnMethods = {"getProductById"})
    public void createProduct(String testCaseId) {
        String url = host + createProduct;
        Properties prop = loadCSVData(testCaseId);

        newProductDTO.setCategoryName(prop.getProperty("categoryName"));
        newProductDTO.setProductDetails(prop.getProperty("productDetails"));
        newProductDTO.setProductName(prop.getProperty("productName"));
        newProductDTO.setProductPrice(Integer.valueOf(prop.getProperty("productPrice")));
        newProductDTO.setQuantity(Integer.valueOf(prop.getProperty("quantity")));
        newProductDTO.setProductPhoto(prop.getProperty("productPhoto"));

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO);

        assertEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"createProduct"})
    public void getProductByName() {
        String url = host + getProductByName;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("productName", Collections.singletonList(newProductDTO.getProductName()));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertEquals(response.getStatus(), 200);

        productsEntity = response.readEntity(ProductsDTO.class).getProductsEntityList().get(0);
        assertTrue(verifyProductInfo(productsEntity, newProductDTO));
    }

    @Test(dependsOnMethods = {"getProductByName"})
    public void getProductsByProviderId() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap);

        ProductsEntity productsEntityFromResponse = response.readEntity(new GenericType<List<ProductsEntity>>() {}).get(0);
        assertEquals(response.getStatus(), 200);
        assertEquals(productsEntity.getId(), productsEntityFromResponse.getId());
    }

    @Test(dataProvider = "modifyProductCSVData", dependsOnMethods = {"getProductsByProviderId"})
    public void modifyProduct(String testCaseId) {
        String url = host + modifyProduct;
        Properties prop = loadCSVData(testCaseId);

        newProductDTO = new NewProductDTO();
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

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO);

        if (testCaseId.equalsIgnoreCase("modifyProductSuccess")) {
            assertEquals(response.getStatus(), 200);
        } else {
            assertNotEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"modifyProduct"})
    public void getProductByIdAfterModified() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(newProductDTO.getProductId())));

        Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);
        assertEquals(response.getStatus(), 200);
        assertFalse(verifyProductInfo(response.readEntity(ProductsEntity.class), newProductDTO));
    }

    @Test(dataProvider = "deleteProductCSVData", dependsOnMethods = {"getProductByIdAfterModified"})
    public void deleteProduct(String testCaseId) {
        String url = host + deleteProduct;
        Properties prop = loadCSVData(testCaseId);

        NewProductDTO newProductDTO = new NewProductDTO();

        if (testCaseId.equalsIgnoreCase("deleteProductFail")) {
            newProductDTO.setProductId(Integer.valueOf(prop.getProperty("productId")));
        } else {
            newProductDTO.setProductId(productsEntity.getId());
        }

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO);

        if (testCaseId.equalsIgnoreCase("deleteProductFail")) {
            assertNotEquals(response.getStatus(), 200);
        } else {
            assertEquals(response.getStatus(), 200);
        }
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductsByProviderId() {
        String url = host + getProductsByProviderId;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("providerId", Collections.singletonList(String.valueOf(testUsersEntity.getId())));

        Response response =  BaseAPIUtil.sendGetRequest(url, jwtModel.getJwt(), multivaluedStringMap);

        List<ProductsEntity> productsEntityList = response.readEntity(new GenericType<List<ProductsEntity>>() {});
        assertEquals(response.getStatus(), 200);
        assertTrue(productsEntityList.isEmpty());
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductById() {
        String url = host + getProductByIdRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(productsEntity.getId())));

        Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);
        assertNotEquals(response.getStatus(), 200);
    }

    @Test(dependsOnMethods = {"deleteProduct"})
    public void getEmptyProductByName() {
        String url = host + getProductByName;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        multivaluedStringMap.put("productName", Collections.singletonList(productsEntity.getProductName()));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap);

        assertTrue(response.readEntity(ProductsDTO.class).getProductsEntityList().isEmpty());
    }

    @Test(dependsOnMethods = {"getEmptyProductsByProviderId"})
    public void deleteProductEmpty() {
        String url = host + deleteProduct;
        NewProductDTO newProductDTO = new NewProductDTO();
        newProductDTO.setProductId(productsEntity.getId());

        Response response =  BaseAPIUtil.sendPostRequest(url, jwtModel.getJwt(), newProductDTO);

        assertNotEquals(response.getStatus(), 200);
    }

    private Boolean verifySort(ProductsDTO productsDTO, String sort) {
        switch (sort) {
            case "newest" -> {
                for (int i = 0; i < productsDTO.getProductsEntityList().size() - 1; i++) {
                    if (productsDTO.getProductsEntityList().get(i).getId() >= productsDTO.getProductsEntityList().get(i + 1).getId()) {
                        return false;
                    }
                }
            }
            case "price_asc" -> {
                for (int i = 0; i < productsDTO.getProductsEntityList().size() - 1; i++) {
                    if (productsDTO.getProductsEntityList().get(i).getProductPrice() > productsDTO.getProductsEntityList().get(i + 1).getProductPrice()) {
                        return false;
                    }
                }
            }
            case "price_desc" -> {
                for (int i = 0; i < productsDTO.getProductsEntityList().size() - 1; i++) {
                    if (productsDTO.getProductsEntityList().get(i).getProductPrice() < productsDTO.getProductsEntityList().get(i + 1).getProductPrice()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private Boolean verifyCategory(ProductsDTO productsDTO, String categoryName) {
        for(ProductsEntity product : productsDTO.getProductsEntityList()) {
            if (!product.getCategoryEntity().getCategoryName().equalsIgnoreCase(categoryName)) {
                return false;
            }
        }
        return true;
    }

    private Boolean verifyProductInfo(ProductsEntity productsEntity, NewProductDTO newProductDTO) {
        NewProductDTO temp = new NewProductDTO();
        newProductDTO.setProductId(null);
        temp.setCategoryName(productsEntity.getCategoryEntity().getCategoryName());
        temp.setProductDetails(productsEntity.getProductDetails());
        temp.setProductName(productsEntity.getProductName());
        temp.setProductPrice(productsEntity.getProductPrice());
        temp.setQuantity(productsEntity.getQuantity());
        temp.setProductPhoto(productsEntity.getProductPhoto());

        if (temp.equals(newProductDTO)) {
            return true;
        } else {
            return false;
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
        return new Object[][]{{"modifyProductFail"}, {"modifyProductSuccess"}};
    }
    @DataProvider(name = "deleteProductCSVData")
    public Object[][] deleteProductCSVData() {
        return new Object[][]{{"deleteProductFail"}, {"deleteProductSuccess"}};
    }

}
