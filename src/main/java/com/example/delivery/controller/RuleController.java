package com.example.delivery.controller;

import com.example.delivery.model.Rule;
import com.example.delivery.service.RuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("rules")
public class RuleController {
    private final RuleServiceImpl ruleService;
    @PostMapping("/rule")
    public void addRule(@RequestBody Rule ruleToAdd) {
        ruleService.addRule(ruleToAdd);
    }
}
