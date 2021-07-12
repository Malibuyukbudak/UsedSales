package com.example.application.services;

import com.example.application.models.Category;
import com.example.application.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return categoryRepository.count();
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);

    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }
}
