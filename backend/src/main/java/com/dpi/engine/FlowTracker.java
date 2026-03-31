package com.dpi.engine;

import com.dpi.model.Flow;
import com.dpi.model.FiveTuple;
import com.dpi.store.AsyncFlowQueue;
import com.dpi.metrics.MetricsCollector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlowTracker {

    private final RuleEngine ruleEngine;
    private final MetricsCollector metricsCollector;

    // Flow storage
    private final Map<FiveTuple, Flow> flowMap = new HashMap<>();
    private final Map<String, Long> blockedIps = new HashMap<>();

    // Time-based port scan tracker
    private final Map<String, Map<Integer, Long>> portScanTracker = new HashMap<>();

    // ✅ SINGLE constructor (correct)
    public FlowTracker(RuleEngine ruleEngine, MetricsCollector metricsCollector) {
        this.ruleEngine = ruleEngine;
        this.metricsCollector = metricsCollector;
    }

    public void processPacket(Flow flow) {
        String srcIp = flow.getFiveTuple().getSrcIp();

        // If already blocked → skip processing
        if (blockedIps.containsKey(srcIp)) {
            flow.setBlocked(true);
            metricsCollector.incrementBlockedFlows();
            AsyncFlowQueue.enqueue(flow);
            return;
        }

        // ===== METRICS: packet =====
        metricsCollector.incrementPackets();

        FiveTuple key = flow.getKey();

        // Store/update flow
        flowMap.put(key, flow);

        // ===== METRICS: flow =====
        metricsCollector.incrementFlows();

        // ===== RULE ENGINE =====
        String action = ruleEngine.evaluate(flow);

        if ("BLOCK".equalsIgnoreCase(action)) {
            flow.setBlocked(true);
            metricsCollector.incrementBlockedFlows();
        }

        // ===== THREAT DETECTION =====
        detectPortScan(flow);

        // ===== ASYNC PIPELINE =====
        AsyncFlowQueue.enqueue(flow);
    }

    private void detectPortScan(Flow flow) {

        String srcIp = flow.getFiveTuple().getSrcIp();
        int dstPort = flow.getFiveTuple().getDstPort();
        long currentTime = System.currentTimeMillis();

        portScanTracker.putIfAbsent(srcIp, new HashMap<>());
        Map<Integer, Long> ports = portScanTracker.get(srcIp);

        // Add/update port with timestamp
        ports.put(dstPort, currentTime);

        // Remove old entries (older than 10 seconds)
        long window = 10_000;
        ports.entrySet().removeIf(entry -> currentTime - entry.getValue() > window);

        // Threshold check + ACTIVE RESPONSE
        if (ports.size() > 20) {

            System.out.println("🚨 PORT SCAN ATTACK from IP: " + srcIp);

            flow.setBlocked(true);
            blockedIps.put(srcIp, System.currentTimeMillis());
            metricsCollector.incrementBlockedFlows();
            metricsCollector.incrementBlockedFlows();
        }
    }

    public Map<FiveTuple, Flow> getFlows() {
        return flowMap;
    }
}