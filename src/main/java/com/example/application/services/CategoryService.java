package com.example.application.services;

import com.example.application.models.Category;
import com.example.application.models.Product;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Long count();

    void delete(Category category);

    void save(Category category);
}
