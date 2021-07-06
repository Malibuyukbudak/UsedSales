package com.example.application.services;

import com.example.application.models.Category;
import com.example.application.models.Product;
import com.example.application.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return productRepository.count();
    }


    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }


    @Override
    public Set<Product> getList() {
        Set<Product> productSet=new HashSet<>();
        productRepository.findAll().iterator().forEachRemaining(productSet::add);
        return productSet;
    }

    @Override
    public Set<Product> getList(String filter) {
        Set<Product> productSet=new HashSet<>();
        productRepository.findByDescriptionContainingIgnoreCase(filter).iterator().forEachRemaining(productSet::add);
        return productSet;
    }

}
