package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.repository.RuleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final RuleRepository ruleRepository;
    private static final String NO_RULE = "No rule exists!";
    private static final String INVALID_INPUT = "Invalid Input!";

    /**
     * Save new rule to the database if parameters correct.
     * @param ruleToAdd to the database.
     */
    @Override
    @Transactional
    public void createRule(Rule ruleToAdd) {
        validateParameters(ruleToAdd);
        ruleRepository.save(ruleToAdd);
    }
    private void validateParameters(Rule ruleToValidate) {
        if (ruleToValidate.getFee() == null || ruleToValidate.getFeeType() == null) {
            throw new BadRequestException(INVALID_INPUT);
        }
    }

    /**
     * Update rule with given id, with given rules variables.
     * @param id of the rule to update.
     * @param updatedRule which has new variables.
     */
    @Override
    @Transactional
    public void updateRule(Long id, Rule updatedRule) {
        Rule existingRule = findRuleById(id);
        BeanUtils.copyProperties(updatedRule, existingRule, "id");
        ruleRepository.save(existingRule);
    }

    /**
     * Delete rule with given id.
     * @param id which rule to delete.
     */
    @Override
    @Transactional
    public void deleteRule(Long id) {
        Rule ruleToDelete = findRuleById(id);
        ruleRepository.delete(ruleToDelete);
    }

    /**
     * Read rule with given id from the database.
     * @param id of rule to read.
     * @return rule with given id.
     */
    @Override
    public Rule readRule(Long id) {
        return findRuleById(id);
    }
    private Rule findRuleById(Long id) {
        return ruleRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException(NO_RULE));
    }

    /**
     * Read rule which has one or more matching parameters.
     * @param feeType the type of fee for the rule.
     * @param city the city where the rule applies.
     * @param vehicle the type of vehicle for the rule.
     * @param value of the weather condition.
     * @param phenomenon the weather phenomenon for the rule.
     * @return rule which has matching parameters.
     */
    public Rule readRuleWithParams(FeeType feeType, City city, Vehicle vehicle,
                                   BigDecimal value, String phenomenon) {
       return ruleRepository.findRule(feeType, city, vehicle, value, phenomenon);
    }

}
