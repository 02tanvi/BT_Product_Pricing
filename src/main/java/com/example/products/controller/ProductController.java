package com.example.products.controller;

import com.example.products.entity.Product;
import com.example.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cdx-api/product-pricing")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts() { return productService.getAllProducts(); }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/code/{code}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/products/status/{status}")
    public List<Product> getProductsByStatus(@PathVariable String status) {
        return productService.getProductsByStatus(status);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.getProductById(id).map(product -> {
            product.setProductCode(productDetails.getProductCode());
            product.setProductName(productDetails.getProductName());
            product.setProductType(productDetails.getProductType());
            product.setEffectiveDate(productDetails.getEffectiveDate());
            product.setExpiryDate(productDetails.getExpiryDate());
            product.setBranch(productDetails.getBranch());
            product.setCurrency(productDetails.getCurrency());
            product.setStatus(productDetails.getStatus());
            product.setDescription(productDetails.getDescription());
            return ResponseEntity.ok(productService.saveProduct(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productService.getProductById(id).map(product -> {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
