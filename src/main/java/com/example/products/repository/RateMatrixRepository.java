package com.example.products.repository;

import com.example.products.entity.RateMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RateMatrixRepository extends JpaRepository<RateMatrix, Long> {

    List<RateMatrix> findByProductId(Long productId);

    List<RateMatrix> findByCustomerCategory(String category);

    List<RateMatrix> findByCreatedAtBeforeAndUpdatedAtAfter(LocalDateTime start, LocalDateTime end);
}
