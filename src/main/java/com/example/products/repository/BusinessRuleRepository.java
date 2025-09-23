package com.example.products.repository;

import com.example.products.entity.BusinessRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessRuleRepository extends JpaRepository<BusinessRule, Long> {
    List<BusinessRule> findByProductId(Long productId);
}
