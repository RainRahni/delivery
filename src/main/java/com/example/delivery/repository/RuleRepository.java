package com.example.delivery.repository;

import com.example.delivery.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface RuleRepository extends JpaRepository<Rule,Long> {
    @Query(value = "SELECT r FROM Rule r " +
            "WHERE r.feeType = :feeType " +
            "AND r.city = :city " +
            "AND r.vehicle = :vehicle " +
            "AND :value BETWEEN r.upperBound AND r.lowerBound")
    Rule findRule(String feeType, String city, String vehicle, BigDecimal value);
}
