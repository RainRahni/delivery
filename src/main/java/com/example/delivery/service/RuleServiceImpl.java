package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.repository.RuleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;
    private static final String NO_RULE = "No rule with this id exists!";
    @Override
    @Transactional
    public void createRule(Rule ruleToAdd) {
        ruleRepository.save(ruleToAdd);
    }
    @Override
    @Transactional
    public void updateRule(Long id, Rule updatedRule) {
        Rule existingRule = findRuleById(id);
        BeanUtils.copyProperties(updatedRule, existingRule, "id");
        ruleRepository.save(existingRule);
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        Rule ruleToDelete = findRuleById(id);
        ruleRepository.delete(ruleToDelete);
    }

    @Override
    public Rule readRule(Long id) {
        return findRuleById(id);
    }
    public Rule findRuleById(Long id) {
        return ruleRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException(NO_RULE));
    }
    public Rule readRuleWithParams(FeeType feeType, City city, Vehicle vehicle,
                                   BigDecimal value, String phenomenon) {
       return ruleRepository.findRule(feeType, city, vehicle, value, phenomenon);
    }

}
