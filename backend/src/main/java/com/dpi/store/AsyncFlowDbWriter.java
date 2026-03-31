package com.dpi.store;

import com.dpi.model.Flow;
import com.dpi.repository.FlowRepository;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsyncFlowDbWriter {

    private final FlowRepository flowRepository;

    private static final int BATCH_SIZE = 50;

    public AsyncFlowDbWriter(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    @PostConstruct
    public void start() {
        Thread worker = new Thread(this::processQueue);
        worker.setDaemon(true);
        worker.start();
    }

    private void processQueue() {

        while (true) {
            try {

                // ✅ 1. Collect batch
                List<Flow> batch = new ArrayList<>();

                batch.add(AsyncFlowQueue.take()); // blocking

                while (batch.size() < BATCH_SIZE && AsyncFlowQueue.size() > 0) {
                    batch.add(AsyncFlowQueue.take());
                }

                // ✅ 2. Process batch
                for (Flow flow : batch) {

                    String flowKey = generateFlowKey(flow);

                    flowRepository.upsertFlow(
                            flowKey,
                            flow.getKey().getSrcIp(),
                            flow.getKey().getDstIp(),
                            flow.getKey().getSrcPort(),
                            flow.getKey().getDstPort(),
                            String.valueOf(flow.getKey().getProtocol()),
                            flow.getDomain(),
                            flow.getAppType(),
                            flow.getPacketCount(),
                            flow.getByteCount(),
                            flow.getStartTime(),
                            System.currentTimeMillis(),
                            flow.isBlocked()
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String generateFlowKey(Flow flow) {

        String srcIp = flow.getKey().getSrcIp();
        String dstIp = flow.getKey().getDstIp();
        int srcPort = flow.getKey().getSrcPort();
        int dstPort = flow.getKey().getDstPort();
        int protocol = flow.getKey().getProtocol();

        String ipPort1 = srcIp + ":" + srcPort;
        String ipPort2 = dstIp + ":" + dstPort;

        if (ipPort1.compareTo(ipPort2) < 0) {
            return ipPort1 + "-" + ipPort2 + "-" + protocol;
        } else {
            return ipPort2 + "-" + ipPort1 + "-" + protocol;
        }
    }
}