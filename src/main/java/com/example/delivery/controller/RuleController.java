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
    @PostMapping("/")
    public void createRule(@RequestBody Rule ruleToAdd) {
        ruleService.createRule(ruleToAdd);
    }
    @GetMapping("/{id}")
    public Rule readRule(@PathVariable Long id) {
        return ruleService.readRule(id);
    }
    @PutMapping("/{id}")
    public void updateRule(@PathVariable Long id, @RequestBody Rule newRule) {
        ruleService.updateRule(id, newRule);
    }
    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }

}
