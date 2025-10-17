package com.example.products.service;

import com.example.products.entity.InterestRate;
import com.example.products.repository.InterestRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InterestRateService {
    @Autowired
    private InterestRateRepository interestRateRepository;

    public List<InterestRate> getAllRates() { return interestRateRepository.findAll(); }
    public List<InterestRate> getRatesByProductId(Long productId) { return interestRateRepository.findByProduct_ProductId(productId); }
    public InterestRate saveRate(InterestRate rate) { return interestRateRepository.save(rate); }
    public void deleteRate(Long id) { interestRateRepository.deleteById(id); }
}
