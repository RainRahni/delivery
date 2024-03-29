package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.repository.RuleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    //TODO: GET RULE BASED ON PARAMETERS
    private final RuleRepository ruleRepository;
    private final String NO_RULE = "No rule with this id exists!";
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
        Rule ruleToDelete = readRule(id);
        ruleRepository.delete(ruleToDelete);
    }

    @Override
    public Rule readRule(Long id) {
        return findRuleById(id);
    }
    private Rule findRuleById(Long id) {
        return ruleRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException(NO_RULE));
    }
    public Rule readRuleWithParams(String feeType, City city, Vehicle vehicle, double value) {
        return readRuleWithParams(feeType, city, vehicle, value);
    }


}
