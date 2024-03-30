package com.example.delivery.controller;

import com.example.delivery.model.Rule;
import com.example.delivery.service.RuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("rules")
public class RuleController {
    private final RuleServiceImpl ruleService;

    /**
     * Create rule by adding it to the database.
     * @param ruleToAdd to the database.
     */
    @PostMapping("/")
    public void createRule(@RequestBody Rule ruleToAdd) {
        ruleService.createRule(ruleToAdd);
    }

    /**
     * Read rule with specific id from the database.
     * @param id which rule to read.
     * @return rule with given id.
     */
    @GetMapping("/{id}")
    public Rule readRule(@PathVariable Long id) {
        return ruleService.readRule(id);
    }

    /**
     * Update rule with given, with new rule's variables.
     * @param id of the rule to update.
     * @param newRule which variables of the rule to update.
     */
    @PutMapping("/{id}")
    public void updateRule(@PathVariable Long id, @RequestBody Rule newRule) {
        ruleService.updateRule(id, newRule);
    }

    /**
     * Delete rule with given id from the database.
     * @param id of the rule to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }

}
