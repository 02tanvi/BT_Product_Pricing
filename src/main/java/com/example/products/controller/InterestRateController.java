package com.example.products.controller;

import com.example.products.entity.InterestRate;
import com.example.products.service.InterestRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cdx-api/product-pricing/interest-rates")
public class InterestRateController {

    @Autowired
    private InterestRateService interestRateService;

    @GetMapping
    public List<InterestRate> getAllRates() {
        return interestRateService.getAllRates();
    }

    @GetMapping("/product/{productId}")
    public List<InterestRate> getRatesByProduct(@PathVariable Long productId) {
        return interestRateService.getRatesByProductId(productId);
    }

    @PostMapping
    public InterestRate createRate(@RequestBody InterestRate rate) {
        return interestRateService.saveRate(rate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        interestRateService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }
}
