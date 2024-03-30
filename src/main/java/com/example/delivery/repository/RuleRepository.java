package com.example.delivery.repository;

import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface RuleRepository extends JpaRepository<Rule,Long> {
    @Query(value = "SELECT r FROM Rule r " +
            "WHERE r.feeType = :feeType " +
            "AND (:city IS NULL OR r.city = :city)" +
            "AND r.vehicle = :vehicle " +
            "AND (:value IS NULL OR :value BETWEEN r.lowerBound AND r.upperBound)" +
            "AND (:phenomenon IS NULL OR UPPER(r.phenomenon) LIKE UPPER(concat('%', :phenomenon, '%')))")
    Rule findRule(FeeType feeType, City city, Vehicle vehicle, BigDecimal value, String phenomenon);
}
