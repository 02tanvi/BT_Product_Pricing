package com.example.products.service;

import com.example.products.entity.BusinessRule;
import com.example.products.repository.BusinessRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessRuleService {

    @Autowired
    private BusinessRuleRepository repository;

    public BusinessRule createBusinessRule(Long productId, BusinessRule rule) {
        rule.setProductId(productId);
        return repository.save(rule);
    }

    public List<BusinessRule> getRulesByProduct(Long productId) {
        return repository.findByProductId(productId);
    }

    public BusinessRule updateBusinessRule(Long ruleId, BusinessRule rule) {
        BusinessRule existing = repository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("BusinessRule not found: " + ruleId));
        
        existing.setMinTerm(rule.getMinTerm());
        existing.setMaxTerm(rule.getMaxTerm());
        existing.setMinAmount(rule.getMinAmount());
        existing.setMaxAmount(rule.getMaxAmount());
        existing.setInterestRate(rule.getInterestRate());
        existing.setCompoundingFrequency(rule.getCompoundingFrequency());
        existing.setPrematureWithdrawalAllowed(rule.getPrematureWithdrawalAllowed());
        existing.setPrematurePenaltyRate(rule.getPrematurePenaltyRate());
        existing.setAutoRenewal(rule.getAutoRenewal());
        existing.setMinBalanceRequired(rule.getMinBalanceRequired());
        
        return repository.save(existing);
    }

    public void deleteBusinessRule(Long ruleId) {
        repository.deleteById(ruleId);
    }
}
