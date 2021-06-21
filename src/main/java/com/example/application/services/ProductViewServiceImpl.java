package com.example.application.services;

import com.example.application.models.ProductView;
import com.example.application.repository.ProductViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class ProductViewServiceImpl implements ProductViewService {
    private final ProductViewRepository productViewRepository;

    public ProductViewServiceImpl(ProductViewRepository productViewRepository) {
        this.productViewRepository = productViewRepository;
    }


    @Override
    public List<ProductView> findAll() {
        return StreamSupport.stream(productViewRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public void save(ProductView productView) {
        productViewRepository.save(productView);
    }

    @Override
    public Long count() {
        return productViewRepository.count();
    }
}
