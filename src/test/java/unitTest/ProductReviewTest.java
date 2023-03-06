package unitTest;

import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;
import com.book.model.UsersEntity;
import com.book.repository.ProductReviewsRepository;
import com.book.service.impl.ProductReviewsService;
import com.book.service.impl.UserService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class ProductReviewTest {
    private ProductReviewsService productReviewsService;
    private ProductReviewsRepository productReviewsRepository;
    private UserService userService;
    private String jwtToken;
    private String authHeader;

    @BeforeClass
    public void setUp() {
        productReviewsRepository = mock(ProductReviewsRepository.class);
        userService = mock(UserService.class);
        productReviewsService = new ProductReviewsService(productReviewsRepository, userService);

        jwtToken = "fakeJwtToken";
        authHeader = "Bearer " + jwtToken;
    }

    @Test
    public void findAllByProductId() {
        List<ProductReviewsEntity> productReviewsEntityList = Arrays.asList(mock(ProductReviewsEntity.class), mock(ProductReviewsEntity.class));
        when(productReviewsRepository.findAllByProductId(anyInt())).thenReturn(productReviewsEntityList);

        assertTrue(productReviewsService.findAllByProductId(0).isPresent());
    }

    @Test
    public void getAverageRatingByProductId() {
        List<ProductReviewsEntity> productReviewsEntityList = Arrays.asList(mock(ProductReviewsEntity.class), mock(ProductReviewsEntity.class));
        when(productReviewsRepository.findAllByProductId(anyInt())).thenReturn(productReviewsEntityList);

        assertTrue(productReviewsService.getAverageRatingByProductId(0).isPresent());

        when(productReviewsRepository.findAllByProductId(anyInt())).thenReturn(new ArrayList<>());

        assertTrue(productReviewsService.getAverageRatingByProductId(0).get().equals(0));
    }

    @Test
    public void saveProductReview() {
        when(userService.getUserByJwtToken(authHeader.substring(7))).thenReturn(Optional.of(mock(UsersEntity.class)));

        assertTrue(productReviewsService.saveProductReview(authHeader, mock(ProductReviewDTO.class)));

        when(userService.getUserByJwtToken(authHeader.substring(7))).thenReturn(Optional.empty());

        assertFalse(productReviewsService.saveProductReview(authHeader, mock(ProductReviewDTO.class)));
    }

    @Test
    public void findAll() {
        List<ProductReviewsEntity> productReviewsEntityList = Arrays.asList(mock(ProductReviewsEntity.class), mock(ProductReviewsEntity.class));
        when(productReviewsRepository.findAll()).thenReturn(productReviewsEntityList);

        assertTrue(productReviewsService.findAll().isPresent());
    }
}
