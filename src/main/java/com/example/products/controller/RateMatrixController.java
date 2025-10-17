package com.example.products.controller;

import com.example.products.entity.RateMatrix;
import com.example.products.service.RateMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cdx-api/product-pricing/rate-matrix")
public class RateMatrixController {

    @Autowired
    private RateMatrixService rateMatrixService;

    @GetMapping
    public List<RateMatrix> getAllRates() {
        return rateMatrixService.getAllRates();
    }

    @GetMapping("/product/{productId}")
    public List<RateMatrix> getRatesByProduct(@PathVariable Long productId) {
        return rateMatrixService.getRatesByProductId(productId);
    }

    @GetMapping("/category/{category}")
    public List<RateMatrix> getRatesByCategory(@PathVariable String category) {
        return rateMatrixService.getRatesByCustomerCategory(category);
    }

    @GetMapping("/active")
    public List<RateMatrix> getActiveRates() {
        LocalDateTime now = LocalDateTime.now(); // Option 1: use LocalDateTime
        return rateMatrixService.getActiveRates(now);
    }

    @PostMapping
    public RateMatrix createRate(@RequestBody RateMatrix rateMatrix) {
        return rateMatrixService.saveRate(rateMatrix);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        rateMatrixService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }
}
