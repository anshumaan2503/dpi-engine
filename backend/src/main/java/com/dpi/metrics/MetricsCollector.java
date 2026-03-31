package com.dpi.metrics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class MetricsCollector {

    private final AtomicLong totalPackets = new AtomicLong(0);
    private final AtomicLong totalFlows = new AtomicLong(0);
    private final AtomicLong blockedFlows = new AtomicLong(0);

    public void incrementPackets() {
        totalPackets.incrementAndGet();
    }

    public void incrementFlows() {
        totalFlows.incrementAndGet();
    }

    public void incrementBlockedFlows() {
        blockedFlows.incrementAndGet();
    }

    public long getTotalPackets() {
        return totalPackets.get();
    }

    public long getTotalFlows() {
        return totalFlows.get();
    }

    public long getBlockedFlows() {
        return blockedFlows.get();
    }
}