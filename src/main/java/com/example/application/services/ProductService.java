package com.example.application.services;

import com.example.application.models.Product;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> findAll();
    Long count();
    void delete(Product product);
    void save(Product product);

    Set<Product> getList();
    Set<Product> getList(String filter);
    Set<Product> getList(Long id);
    Set<Product> getListCategory(String categoryType);


}
