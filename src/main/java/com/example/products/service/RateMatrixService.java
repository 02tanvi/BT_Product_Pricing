package com.example.products.service;

import com.example.products.entity.RateMatrix;
import com.example.products.repository.RateMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RateMatrixService {

    @Autowired
    private RateMatrixRepository rateMatrixRepository;

    public RateMatrix saveRate(RateMatrix rateMatrix) {
        return rateMatrixRepository.save(rateMatrix);
    }

    public List<RateMatrix> getAllRates() {
        return rateMatrixRepository.findAll();
    }

    public List<RateMatrix> getRatesByProductId(Long productId) {
        return rateMatrixRepository.findByProductId(productId);
    }

    public List<RateMatrix> getRatesByCustomerCategory(String category) {
        return rateMatrixRepository.findByCustomerCategory(category);
    }

    public List<RateMatrix> getActiveRates(LocalDateTime now) {
        // Return rates where current timestamp is between created_at and updated_at
        return rateMatrixRepository.findByCreatedAtBeforeAndUpdatedAtAfter(now, now);
    }

    public void deleteRate(Long id) {
        rateMatrixRepository.deleteById(id);
    }
}
