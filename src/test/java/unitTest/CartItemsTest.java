package unitTest;

import com.book.dto.CartItemsDTO;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.repository.CartItemsRepository;
import com.book.service.impl.CartItemsService;
import com.book.service.impl.ProductsService;
import com.book.service.impl.UserService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CartItemsTest {
    private CartItemsRepository cartItemsRepository;
    private UserService userService;
    private ProductsService productsService;
    private CartItemsService cartItemsService;
    private CartItemsDTO cartItemsDTO;
    private UsersEntity usersEntity;
    private ProductsEntity productsEntity;
    private String jwtToken;
    private String authHeader;

    @BeforeClass
    public void setUp() {
        cartItemsRepository = mock(CartItemsRepository.class);
        userService = mock(UserService.class);
        productsService = mock(ProductsService.class);
        cartItemsService = new CartItemsService(cartItemsRepository, userService, productsService);
        usersEntity = new UsersEntity();
        usersEntity.setUserName("testuser");
        usersEntity.setPassword("testpassword");
        cartItemsDTO = new CartItemsDTO(0, 0);
        productsEntity = new ProductsEntity();
        productsEntity.setId(0);
        jwtToken = "fakeJwtToken";
        authHeader = "Bearer " + jwtToken;
    }

    @Test
    public void saveCartItemTestSuccessful() {
        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(usersEntity));
        when(productsService.findProductById(cartItemsDTO.getProductId())).thenReturn(Optional.of(productsEntity));

        assertTrue(cartItemsService.saveCartItem(authHeader, cartItemsDTO));
    }

    @Test
    public void saveCartItemTestFailed() {
        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.empty());
        when(productsService.findProductById(cartItemsDTO.getProductId())).thenReturn(Optional.of(productsEntity));

        assertFalse(cartItemsService.saveCartItem(authHeader, cartItemsDTO));

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(usersEntity));
        when(productsService.findProductById(cartItemsDTO.getProductId())).thenReturn(Optional.empty());

        assertFalse(cartItemsService.saveCartItem(authHeader, cartItemsDTO));
    }
}
