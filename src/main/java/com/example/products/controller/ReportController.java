package com.example.products.controller;

import com.example.products.entity.Product;
import com.example.products.service.BusinessRuleService;
import com.example.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cdx-api/reports")
public class ReportController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BusinessRuleService businessRuleService;

    // -------------------- Product List Report --------------------
    // Filter by creation date range and/or status
    @GetMapping("/product-list")
    public ResponseEntity<List<Product>> productList(
            @RequestParam(value = "start", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(value = "end", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(value = "status", required = false) String status
    ) {
        List<Product> products = productService.getAllProducts();

        if (start != null && end != null) {
            products = productService.getProductsBetween(start, end);
        }

        if (status != null && !status.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        return ResponseEntity.ok(products);
    }

    // -------------------- Active Products Report --------------------
    @GetMapping("/active-products")
    public ResponseEntity<List<Product>> activeProducts(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("productType") String productType
    ) {
        List<Product> activeProducts = productService.getActiveProducts(date, productType);
        return ResponseEntity.ok(activeProducts);
    }

    // -------------------- Interest Rate Report --------------------
    @GetMapping("/interest-rate/{productId}")
    public ResponseEntity<List<BigDecimal>> interestRateForProduct(@PathVariable Long productId) {
        List<BigDecimal> rates = businessRuleService.getInterestRatesByProduct(productId);
        return ResponseEntity.ok(rates);
    }
}
