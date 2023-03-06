package com.book.service.impl;

import com.book.model.CategoryEntity;
import com.book.repository.CategoryRepository;
import com.book.service.CategoryServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    public Optional<List<CategoryEntity>> findAllCategories() {
        return Optional.of(categoryRepository.findAll());
    }
    public Optional<CategoryEntity> findCategoryEntityByCategoryName(String categoryName) {
        return categoryRepository.findCategoryEntityByCategoryName(categoryName);
    }
}
