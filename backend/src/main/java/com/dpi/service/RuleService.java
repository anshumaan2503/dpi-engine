package com.dpi.service;

import com.dpi.entity.RuleEntity;
import com.dpi.repository.RuleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    // In-memory cache
    private List<RuleEntity> cachedRules;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    // Load rules at startup
    @PostConstruct
    public void loadRules() {
        this.cachedRules = ruleRepository.findAll();
    }

    // Only active rules (USED BY RULE ENGINE)
    public List<RuleEntity> getActiveRules() {
        return cachedRules.stream()
                .filter(RuleEntity::isEnabled)
                .collect(Collectors.toList());
    }

    // All rules (for API)
    public List<RuleEntity> getAllRules() {
        return cachedRules;
    }

    // Save + refresh cache
    public RuleEntity saveRule(RuleEntity rule) {
        RuleEntity saved = ruleRepository.save(rule);
        loadRules(); // refresh cache
        return saved;
    }

    // Delete + refresh cache
    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
        loadRules(); // refresh cache
    }
}