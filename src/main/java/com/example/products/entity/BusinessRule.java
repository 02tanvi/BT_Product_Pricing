package com.example.products.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_rules")
public class BusinessRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ruleId")
    private Long ruleId;

    @Column(nullable = false)
    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("minTerm")
    private Integer minTerm;

    @JsonProperty("maxTerm")
    private Integer maxTerm;

    @JsonProperty("minAmount")
    private BigDecimal minAmount;

    @JsonProperty("maxAmount")
    private BigDecimal maxAmount;

    @JsonProperty("interestRate")
    private BigDecimal interestRate;

    @JsonProperty("compoundingFrequency")
    private String compoundingFrequency;

    @JsonProperty("prematureWithdrawalAllowed")
    private Boolean prematureWithdrawalAllowed;

    @JsonProperty("prematurePenaltyRate")
    private BigDecimal prematurePenaltyRate;

    @JsonProperty("autoRenewal")
    private Boolean autoRenewal;

    @JsonProperty("minBalanceRequired")
    private BigDecimal minBalanceRequired;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters (same as before)
}
