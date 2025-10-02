package com.example.products.repository;

import com.example.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Use the entity field name 'productName'
    List<Product> findByProductNameContainingIgnoreCase(String productName);
}
