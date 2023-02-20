package unitTest;

import com.book.dto.OrdersDTO;
import com.book.exception.InvalidQuantityException;
import com.book.model.CartItemsEntity;
import com.book.model.ProductsEntity;
import com.book.model.UsersEntity;
import com.book.repository.OrdersRepository;
import com.book.service.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class OrderTest {
    private OrdersRepository ordersRepository;
    private UserService userService;
    private OrderItemsService orderItemsService;
    private CartItemsService cartItemsService;
    private ProductsService productsService;
    private OrdersService ordersService;
    private String jwtToken;
    private String authHeader;

    @BeforeClass
    public void setUp() {
        ordersRepository = mock(OrdersRepository.class);
        userService = mock(UserService.class);
        orderItemsService = mock(OrderItemsService.class);
        cartItemsService = mock(CartItemsService.class);
        productsService = mock(ProductsService.class);
        ordersService = new OrdersService(ordersRepository, userService, orderItemsService, cartItemsService, productsService);
        jwtToken = "fakeJwtToken";
        authHeader = "Bearer " + jwtToken;
    }

    @Test
    public void createOrderFromCart() throws InvalidQuantityException {
        CartItemsEntity mockCartItemsEntity1 = mock(CartItemsEntity.class);
        CartItemsEntity mockCartItemsEntity2 = mock(CartItemsEntity.class);
        when(mockCartItemsEntity1.getProductsEntity()).thenReturn(mock(ProductsEntity.class));
        when(mockCartItemsEntity2.getProductsEntity()).thenReturn(mock(ProductsEntity.class));
        List<CartItemsEntity> cartItemsEntityList = Arrays.asList(mockCartItemsEntity1, mockCartItemsEntity2);

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(mock(UsersEntity.class)));
        when(cartItemsService.getCartItemsEntitiesByUserId(anyInt())).thenReturn(Optional.of(cartItemsEntityList));
        assertTrue(ordersService.createOrderFromCart(authHeader));

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.empty());
        assertFalse(ordersService.createOrderFromCart(authHeader));

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(mock(UsersEntity.class)));
        when(cartItemsService.getCartItemsEntitiesByUserId(anyInt())).thenReturn(Optional.empty());
        assertFalse(ordersService.createOrderFromCart(authHeader));
    }

    @Test
    public void createOrderFromProductId() {
        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(mock(UsersEntity.class)));
        when(productsService.findProductById(anyInt())).thenReturn(Optional.of(mock(ProductsEntity.class)));
        assertTrue(ordersService.createOrderFromProductId(authHeader, mock(OrdersDTO.class)));

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.empty());
        assertFalse(ordersService.createOrderFromProductId(authHeader, mock(OrdersDTO.class)));

        when(userService.getUserByJwtToken(jwtToken)).thenReturn(Optional.of(mock(UsersEntity.class)));
        when(productsService.findProductById(anyInt())).thenReturn(Optional.empty());
        assertFalse(ordersService.createOrderFromProductId(authHeader, mock(OrdersDTO.class)));
    }
}
