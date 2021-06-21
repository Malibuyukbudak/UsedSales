package com.example.application.services;

import com.example.application.models.Product;
import com.example.application.repository.ProductRepository;
import com.example.application.repository.UserRepository;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Long count();

    void delete(Product product);

    void save(Product product);
}
