package com.example.application.repository;

import com.example.application.models.Product;
import com.example.application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Long>, JpaRepository<Product, Long> {
    List<Product> findByDescriptionContainingIgnoreCase (String filter);




}
