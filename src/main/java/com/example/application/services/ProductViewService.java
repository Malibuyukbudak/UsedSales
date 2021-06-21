package com.example.application.services;

import com.example.application.models.ProductView;
import com.example.application.models.User;

import java.util.List;

public interface ProductViewService {
    List<ProductView> findAll();
    void save(ProductView productView);
    Long count();
}
