package com.book.service;

import com.book.model.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryServiceInterface {
    Optional<List<CategoryEntity>> findAllCategories();
}
