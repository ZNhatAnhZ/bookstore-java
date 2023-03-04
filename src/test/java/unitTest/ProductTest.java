package unitTest;

import com.book.dto.NewProductDTO;
import com.book.dto.RatingObjectDTO;
import com.book.model.CategoryEntity;
import com.book.model.ProductReviewsEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.repository.ProductsRepository;
import com.book.service.impl.CategoryService;
import com.book.service.impl.ProductReviewsService;
import com.book.service.impl.ProductsService;
import com.book.service.impl.UserService;
import com.book.util.JwtUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public class ProductTest {
    private ProductsRepository productsRepository;
    private CategoryService categoryService;
    private ProductReviewsService productReviewsService;
    private JwtUtils jwtUtils;
    private UserService userService;
    private ProductsService productsService;
    private List<ProductReviewsEntity> productReviewsEntityList = new ArrayList<>();
    private int page = 0;
    private int size = 15;

    @BeforeClass
    public void setUp() {
        productsRepository = mock(ProductsRepository.class);
        categoryService = mock(CategoryService.class);
        productReviewsService = mock(ProductReviewsService.class);
        jwtUtils = mock(JwtUtils.class);
        userService = mock(UserService.class);
        productsService = new ProductsService(productsRepository, categoryService, productReviewsService, jwtUtils, userService);
    }

    @Test
    public void findAllProducts() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAll(PageRequest.of(page, size))).thenReturn(mockPage);

        assertTrue(productsService.findAllProducts(page, size).isPresent());
    }

    @Test
    public void findAllProductsSortedByIdDesc() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))).thenReturn(mockPage);

        assertTrue(productsService.findAllProductsSortedByIdDesc(page, size).isPresent());
    }

    @Test
    public void findAllProductsSortedByPriceAsc() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").ascending()))).thenReturn(mockPage);

        assertTrue(productsService.findAllProductsSortedByPriceAsc(page, size).isPresent());
    }

    @Test
    public void findAllProductsSortedByPriceDesc() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").descending()))).thenReturn(mockPage);

        assertTrue(productsService.findAllProductsSortedByPriceDesc(page, size).isPresent());
    }

    @Test
    public void findAllProductsSortedByOrderItemsCount() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAllByTotalSold(PageRequest.of(page, size))).thenReturn(mockPage);

        assertTrue(productsService.findAllProductsSortedByOrderItemsCount(page, size).isPresent());
    }

    @Test
    public void findAllProductsOfCategory() {
        Page<ProductsEntity> mockPage = mock(Page.class);

        when(categoryService.findCategoryEntityByCategoryName("dummyCategoryName")).thenReturn(Optional.of(mock(CategoryEntity.class)));
        when(productsRepository.findAllByCategoryEntityId(page, PageRequest.of(page, size))).thenReturn(mockPage);

        assertTrue(productsService.findAllProductsOfCategory(page, size, "dummyCategoryName").isPresent());

        when(categoryService.findCategoryEntityByCategoryName("nonExistingCategoryName")).thenReturn(Optional.empty());
        when(productsRepository.findAllByCategoryEntityId(page, PageRequest.of(page, size))).thenReturn(mockPage);

        assertFalse(productsService.findAllProductsOfCategory(page, size, "nonExistingCategoryName").isPresent());
    }

    @Test
    public void findProductById() {
        when(productsRepository.findById(any())).thenReturn(Optional.of(mock(ProductsEntity.class)));

        assertTrue(productsService.findProductById(0).isPresent());
    }

    @Test
    public void findAllByProductNameContainingIgnoreCase() {
        Page<ProductsEntity> mockPage = mock(Page.class);
        when(productsRepository.findAllByProductNameContainingIgnoreCase(PageRequest.of(page, size), "dummyname")).thenReturn(mockPage);

        assertTrue(productsService.findAllByProductNameContainingIgnoreCase(page, size,"dummyname").isPresent());
    }

    @Test
    public void getRatingObjectDTO() {
        for(int i=0; i < 5; i++) {
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setId(i);
            ProductReviewsEntity productReviewsEntity = new ProductReviewsEntity();
            productReviewsEntity.setUsersEntity(usersEntity);
            productReviewsEntity.setRating(i);
            productReviewsEntity.setProductId(0);

            productReviewsEntityList.add(productReviewsEntity);
        }

        List<RatingObjectDTO> result = productsService.getRatingObjectDTO(productReviewsEntityList);

        for(int i=0; i < 5; i++) {
            assertNotNull(result.get(i).getProductId());
            assertNotNull(result.get(i).getRating());
            assertNotNull(result.get(i).getUserId());
        }
    }

    @Test(dependsOnMethods = {"getRatingObjectDTO"})
    public void getRecommendedProducts() throws IOException {
        when(productReviewsService.findAll()).thenReturn(Optional.empty());

        assertFalse(productsService.getRecommendedProducts(0).isPresent());
    }

    @Test
    public void findAllByUsersEntityId() {
        List<ProductsEntity> productsEntityList = Arrays.asList(mock(ProductsEntity.class), mock(ProductsEntity.class), mock(ProductsEntity.class));
        when(productsRepository.findAllByUsersEntityId(0)).thenReturn(Optional.of(productsEntityList));

        assertTrue(productsService.findAllByUsersEntityId(0).isPresent());
    }

    @Test
    public void createProduct() {
        UsersEntity usersEntity = mock(UsersEntity.class);
        when(userService.getUserByJwtToken("auth header".substring(7))).thenReturn(Optional.of(usersEntity));

        NewProductDTO newProductDTO = mock(NewProductDTO.class);
        newProductDTO.setCategoryName("fake category");
        when(productsRepository.save(mock(ProductsEntity.class))).thenReturn(mock(ProductsEntity.class));

        assertTrue(productsService.createProduct("auth header", new NewProductDTO()));

        when(categoryService.findCategoryEntityByCategoryName("fake category")).thenReturn(Optional.of(mock(CategoryEntity.class)));
        assertTrue(productsService.createProduct("auth header", new NewProductDTO()));

        when(userService.getUserByJwtToken("auth header".substring(7))).thenReturn(Optional.empty());
        assertFalse(productsService.createProduct("auth header", new NewProductDTO()));
    }

    @Test
    public void modifyProduct() {
        NewProductDTO newProductDTO = mock(NewProductDTO.class);
        newProductDTO.setProductId(0);
        when(productsRepository.findById(newProductDTO.getProductId())).thenReturn(Optional.of(mock(ProductsEntity.class)));

        assertTrue(productsService.modifyProduct(newProductDTO));

        newProductDTO.setCategoryName("fake category");
        when(categoryService.findCategoryEntityByCategoryName("fake category")).thenReturn(Optional.of(mock(CategoryEntity.class)));

        assertTrue(productsService.modifyProduct(newProductDTO));

        when(productsRepository.findById(newProductDTO.getProductId())).thenReturn(Optional.empty());
        assertFalse(productsService.modifyProduct(newProductDTO));
    }

    @Test
    public void deleteProduct() {
        NewProductDTO newProductDTO = mock(NewProductDTO.class);
        newProductDTO.setProductId(0);
        productsService.deleteProduct(newProductDTO);

        verify(productsRepository).deleteById(any());
    }
}
