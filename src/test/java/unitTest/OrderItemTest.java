package unitTest;

import com.book.exception.InvalidQuantityException;
import com.book.model.CartItemsEntity;
import com.book.model.OrdersEntity;
import com.book.model.ProductsEntity;
import com.book.repository.OrderItemsRepository;
import com.book.service.CartItemsService;
import com.book.service.OrderItemsService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class OrderItemTest {
    private OrderItemsRepository orderItemsRepository;
    private CartItemsService cartItemsService;
    private OrderItemsService orderItemsService;

    @BeforeClass
    public void setUp() {
        orderItemsRepository = mock(OrderItemsRepository.class);
        cartItemsService = mock(CartItemsService.class);
        orderItemsService = new OrderItemsService(orderItemsRepository, cartItemsService);
    }

    @Test
    public void createOrderItemsEntityByCartItemEntityList() throws InvalidQuantityException {
        CartItemsEntity mockCartItemsEntity = mock(CartItemsEntity.class);
        ProductsEntity productsEntity = mock(ProductsEntity.class);
        when(productsEntity.getQuantity()).thenReturn(5);
        when(mockCartItemsEntity.getProductsEntity()).thenReturn(productsEntity);
        when(mockCartItemsEntity.getQuantity()).thenReturn(3);
        List<CartItemsEntity> cartItemsEntityList = List.of(mockCartItemsEntity);

        assertTrue(orderItemsService.createOrderItemsEntityByCartItemEntityList(mock(OrdersEntity.class), cartItemsEntityList));

        when(productsEntity.getQuantity()).thenReturn(2);
        assertThrows(InvalidQuantityException.class, () -> {
            orderItemsService.createOrderItemsEntityByCartItemEntityList(mock(OrdersEntity.class), cartItemsEntityList);
        });
    }

    @Test
    public void createOrderItemsEntityByProductEntity() throws InvalidQuantityException {
        ProductsEntity productsEntity = mock(ProductsEntity.class);
        when(productsEntity.getQuantity()).thenReturn(5);
        assertTrue(orderItemsService.createOrderItemsEntityByProductEntity(mock(OrdersEntity.class), productsEntity, 3));

        when(productsEntity.getQuantity()).thenReturn(2);
        assertThrows(InvalidQuantityException.class, () -> {
            orderItemsService.createOrderItemsEntityByProductEntity(mock(OrdersEntity.class), productsEntity, 3);
        });
    }
}
