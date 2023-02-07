package com.book.service;

import com.book.model.ProductsEntity;
import com.book.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsService implements ProductsServiceInterface{
    private final ProductsRepository productsRepository;

    public Optional<List<ProductsEntity>> findAllProducts() {
        return Optional.of(productsRepository.findAll());
    }
}
