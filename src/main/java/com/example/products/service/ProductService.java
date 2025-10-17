package com.example.products.service;

import com.example.products.entity.Product;
import com.example.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ----------------- CRUD operations -----------------

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByStatus(String status) {
        return productRepository.findByStatus(status);
    }

    public Product getProductByCode(String code) {
        return productRepository.findByProductCode(code);
    }

    // ----------------- Report-related methods -----------------

    public List<Product> getProductsCreatedToday() {
        LocalDate today = LocalDate.now();
        return productRepository.findAll().stream()
                .filter(p -> p.getCreatedAt().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsCreatedThisMonth() {
        LocalDate now = LocalDate.now();
        return productRepository.findAll().stream()
                .filter(p -> p.getCreatedAt().getMonth() == now.getMonth() &&
                             p.getCreatedAt().getYear() == now.getYear())
                .collect(Collectors.toList());
    }

    public List<Product> getProductsBetween(LocalDate start, LocalDate end) {
        return productRepository.findAll().stream()
                .filter(p -> !p.getCreatedAt().toLocalDate().isBefore(start) &&
                             !p.getCreatedAt().toLocalDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Product> getActiveProducts(LocalDate date, String productType) {
        return productRepository.findAll().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(productType) &&
                             !p.getEffectiveDate().isAfter(date) &&
                             (p.getExpiryDate() == null || !p.getExpiryDate().isBefore(date)))
                .collect(Collectors.toList());
    }
}
