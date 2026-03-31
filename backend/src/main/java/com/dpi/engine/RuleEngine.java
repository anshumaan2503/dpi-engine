package com.dpi.engine;

import com.dpi.entity.RuleEntity;
import com.dpi.model.Flow;
import com.dpi.service.RuleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {

    private final RuleService ruleService;

    public RuleEngine(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    // ===== CORE METHOD USED BY FLOW TRACKER =====
    public String evaluate(Flow flow) {

        List<RuleEntity> rules = ruleService.getActiveRules();

        for (RuleEntity rule : rules) {
            if (matchRule(rule, flow)) {
                return rule.getAction(); // ALLOW / BLOCK / ALERT
            }
        }

        return "ALLOW"; // default
    }

    // ===== RULE MATCHING =====
    private boolean matchRule(RuleEntity rule, Flow flow) {

        String field = rule.getField();
        String operator = rule.getOperator();
        String value = rule.getValue();

        if (field == null || operator == null || value == null) {
            return false;
        }

        String fieldValue = extractFieldValue(field, flow);

        if (fieldValue == null) {
            return false;
        }

        switch (operator.toLowerCase()) {

            case "equals":
                return fieldValue.equalsIgnoreCase(value);

            case "contains":
                return fieldValue.toLowerCase().contains(value.toLowerCase());

            case "starts_with":
                return fieldValue.toLowerCase().startsWith(value.toLowerCase());

            default:
                return false;
        }
    }

    // ===== FIELD EXTRACTION =====
    private String extractFieldValue(String field, Flow flow) {

        switch (field.toLowerCase()) {

            case "srcip":
                return flow.getFiveTuple().getSrcIp();

            case "dstip":
                return flow.getFiveTuple().getDstIp();

            case "domain":
                return flow.getDomain();

            case "port":
                return String.valueOf(flow.getFiveTuple().getDstPort());

            default:
                return null;
        }
    }
}