package com.example.delivery.service;

import com.example.delivery.model.Rule;
import com.example.delivery.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;
    @Override
    public void addRule(Rule ruleToAdd) {

    }
}
