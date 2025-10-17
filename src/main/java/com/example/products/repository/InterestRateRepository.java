package com.example.products.repository;

import com.example.products.entity.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterestRateRepository extends JpaRepository<InterestRate, Long> {
    List<InterestRate> findByProduct_ProductId(Long productId);
}
