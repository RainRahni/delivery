package com.example.delivery.repository;

import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RuleRepository extends JpaRepository<Rule,Long> {
    @Query
    Rule findRuleByAllParams(String feeType, City city, Vehicle vehicle, double value);
}
