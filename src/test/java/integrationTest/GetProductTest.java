package integrationTest;

import com.book.dto.ProductsDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductsEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.BaseAPIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class GetProductTest extends BaseTest{
    private String getProductsRoute;
    private String getProductsByCategoryRoute;
    private String getProductByIdRoute;
    private List<ProductsEntity> productsEntityList = new ArrayList<>();
    private List<CategoryEntity> categoryEntityList;
    private long totalNumberOfProducts;
    private String getAllCategoryRoute;

    @BeforeClass
    public void setUp() {
        getProductsRoute = properties.getProperty("getProductsRoute");
        getProductsByCategoryRoute = properties.getProperty("getProductsByCategoryRoute");
        getProductByIdRoute = properties.getProperty("getProductByIdRoute");
        getAllCategoryRoute = properties.getProperty("getAllCategoryRoute");
        testCSVFile = "productTest.csv";
    }

    @Test(dataProvider = "getAllProductsCSVData")
    public void getAllProducts(String sort) {
        String url = host + getProductsRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();
        multivaluedStringMap.put("sort", Collections.singletonList(sort));

        Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);

        ProductsDTO productsDTO = response.readEntity(ProductsDTO.class);

        if (productsEntityList.isEmpty()) {
            productsEntityList = productsDTO.getProductsEntityList();
            totalNumberOfProducts = productsDTO.getTotalItems();
        }

        assertEquals(totalNumberOfProducts, productsDTO.getTotalItems());
        assertTrue(verifySort(productsDTO, sort));
    }

    @Test(dependsOnMethods = {"getAllProducts"})
    public void getProductsByCategory() {
        String url = host + getProductsByCategoryRoute;
        String urlGetCategory = host+getAllCategoryRoute;
        MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

        Response categoryResponse =  BaseAPIUtil.sendGetRequest(urlGetCategory, "", new MultivaluedStringMap(), 200);
        categoryEntityList = categoryResponse.readEntity(new GenericType<List<CategoryEntity>>() {});

        categoryEntityList.forEach((categoryEntity -> {
            multivaluedStringMap.put("categoryName", Collections.singletonList(categoryEntity.getCategoryName()));
            Response response =  BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
            ProductsDTO productsDTO = response.readEntity(ProductsDTO.class);
            assertTrue(verifyCategory(productsDTO, categoryEntity.getCategoryName()));
        }));
    }

    @Test(dependsOnMethods = {"getAllProducts"})
    public void getProductById() {
        String url = host + getProductByIdRoute;
        for(ProductsEntity product : productsEntityList) {
            MultivaluedStringMap multivaluedStringMap = new MultivaluedStringMap();

            multivaluedStringMap.put("id", Collections.singletonList(String.valueOf(product.getId())));

            Response response = BaseAPIUtil.sendGetRequest(url, "", multivaluedStringMap, 200);
            assertEquals(response.readEntity(ProductsEntity.class).getId(), product.getId());
        }
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

    @DataProvider(name = "getAllProductsCSVData")
    public Object[][] getAllProductsCSVData() {
        return new Object[][]{{"getProductDefault"}, {"getProductNewest"}, {"getProductPrice_asc"}, {"getProductPrice_desc"}, {"getProductBest_seller"}};
    }
}
