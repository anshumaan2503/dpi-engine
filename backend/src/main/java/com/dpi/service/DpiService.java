package com.dpi.service;

import com.dpi.dto.FlowResponse;
import com.dpi.dto.StatsResponse;
import com.dpi.dto.DomainStatsResponse;
import com.dpi.engine.RuleEngine;
import com.dpi.engine.PcapReader;
import com.dpi.model.Flow;
import com.dpi.repository.FlowRepository;
import com.dpi.store.FlowStore;
import org.springframework.stereotype.Service;
import com.dpi.engine.PacketWorker;
import java.util.List;

@Service
public class DpiService {

    private final RuleEngine ruleEngine;
    private final FlowRepository flowRepository;
    private final PacketWorker packetWorker;

    // ✅ ONLY ONE CONSTRUCTOR (correct DI)
    public DpiService(RuleEngine ruleEngine,
                      FlowRepository flowRepository,
                      PacketWorker packetWorker) {

        this.ruleEngine = ruleEngine;
        this.flowRepository = flowRepository;
        this.packetWorker = packetWorker;

        // ✅ start worker thread
        new Thread(packetWorker).start();
    }

    public void readPcapFile(String filePath) {
        PcapReader reader = new PcapReader();
        reader.readFile(filePath);
    }

    // 🔥 Flows
    public List<FlowResponse> getFlows(String domain, Boolean blocked, int page, int size) {

        List<Flow> filtered = FlowStore.getInstance()
                .getAllFlows()
                .stream()
                .filter(flow -> domain == null ||
                        (flow.getDomain() != null &&
                                flow.getDomain().toLowerCase().contains(domain.toLowerCase())))
                .filter(flow -> blocked == null || flow.isBlocked() == blocked)
                .sorted((a, b) -> Long.compare(b.getByteCount(), a.getByteCount()))
                .toList();

        int start = page * size;
        int end = Math.min(start + size, filtered.size());

        if (start >= filtered.size()) {
            return List.of();
        }

        return filtered.subList(start, end)
                .stream()
                .map(FlowResponse::new)
                .toList();
    }

    public List<FlowResponse> getBlockedFlows() {
        return FlowStore.getInstance()
                .getAllFlows()
                .stream()
                .filter(Flow::isBlocked)
                .map(FlowResponse::new)
                .toList();
    }

    public List<FlowResponse> getTopFlows() {
        return FlowStore.getInstance()
                .getAllFlows()
                .stream()
                .sorted((a, b) -> Long.compare(b.getByteCount(), a.getByteCount()))
                .limit(10)
                .map(FlowResponse::new)
                .toList();
    }

    public void clearFlows() {
        FlowStore.getInstance().clear();
    }

    public StatsResponse getStats() {
        var flows = FlowStore.getInstance().getAllFlows();

        long totalFlows = flows.size();
        long blockedFlows = flows.stream().filter(Flow::isBlocked).count();
        long totalPackets = flows.stream().mapToLong(Flow::getPacketCount).sum();
        long totalBytes = flows.stream().mapToLong(Flow::getByteCount).sum();

        return new StatsResponse(totalFlows, blockedFlows, totalPackets, totalBytes);
    }

    public List<DomainStatsResponse> getTopDomains() {
        return FlowStore.getInstance()
                .getAllFlows()
                .stream()
                .filter(flow -> flow.getDomain() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        Flow::getDomain,
                        java.util.stream.Collectors.summingLong(Flow::getByteCount)
                ))
                .entrySet()
                .stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(entry -> new DomainStatsResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
}