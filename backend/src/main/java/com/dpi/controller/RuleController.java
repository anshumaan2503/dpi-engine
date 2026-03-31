package com.dpi.controller;

import com.dpi.entity.RuleEntity;
import com.dpi.service.RuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    // CREATE
    @PostMapping
    public RuleEntity createRule(@RequestBody RuleEntity rule) {

        validateRule(rule);
        return ruleService.saveRule(rule);
    }

    // READ ALL
    @GetMapping
    public List<RuleEntity> getAllRules() {
        return ruleService.getAllRules();
    }

    // UPDATE
    @PutMapping("/{id}")
    public RuleEntity updateRule(@PathVariable Long id, @RequestBody RuleEntity rule) {

        rule.setId(id);
        validateRule(rule);
        return ruleService.saveRule(rule);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }

    private void validateRule(RuleEntity rule) {

        if (rule.getProtocol() == null) {
            throw new RuntimeException("Protocol is required");
        }

        if (rule.getAction() == null || rule.getAction().isEmpty()) {
            throw new RuntimeException("Action is required");
        }

        // Optional but smart checks
        if (rule.getPort() != null && (rule.getPort() < 0 || rule.getPort() > 65535)) {
            throw new RuntimeException("Invalid port");
        }
    }
}