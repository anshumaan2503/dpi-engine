package com.dpi.controller;

import com.dpi.metrics.MetricsCollector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsCollector metricsCollector;

    public MetricsController(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    @GetMapping("/api/metrics")
    public Map<String, Object> getMetrics() {

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalPackets", metricsCollector.getTotalPackets());
        metrics.put("totalFlows", metricsCollector.getTotalFlows());
        metrics.put("blockedFlows", metricsCollector.getBlockedFlows());

        return metrics;
    }
}