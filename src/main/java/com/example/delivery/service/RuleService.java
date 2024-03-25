package com.example.delivery.service;

import com.example.delivery.model.Rule;
import org.springframework.stereotype.Service;

@Service
public interface RuleService {
    void addRule(Rule ruleToAdd);
}
