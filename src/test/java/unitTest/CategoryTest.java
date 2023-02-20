package unitTest;

import com.book.model.CategoryEntity;
import com.book.repository.CategoryRepository;
import com.book.service.CategoryService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CategoryTest {
    private CategoryService categoryService;
    private CategoryRepository categoryRepository;

    @BeforeClass
    public void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void findAllCategories() {
        List<CategoryEntity> categoryEntityList = Arrays.asList(mock(CategoryEntity.class), mock(CategoryEntity.class));
        when(categoryRepository.findAll()).thenReturn(categoryEntityList);

        assertTrue(categoryService.findAllCategories().isPresent());
    }

    @Test
    public void findCategoryEntityByCategoryName() {
        when(categoryRepository.findCategoryEntityByCategoryName(anyString())).thenReturn(Optional.of(mock(CategoryEntity.class)));

        assertTrue(categoryService.findCategoryEntityByCategoryName("a").isPresent());

        when(categoryRepository.findCategoryEntityByCategoryName(anyString())).thenReturn(Optional.empty());
        assertTrue(categoryService.findCategoryEntityByCategoryName("a").isEmpty());
    }
}
