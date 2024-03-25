package com.example.delivery.service;

import com.example.delivery.model.Rule;
import org.springframework.stereotype.Service;

@Service
public interface RuleService {
    void createRule(Rule ruleToAdd);

    void updateRule(Long id, Rule newRule);

    void deleteRule(Long id);

    Rule readRule(Long id);
}
