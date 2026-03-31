package com.dpi.engine;

import com.dpi.model.Flow;
import com.dpi.model.FiveTuple;
import org.springframework.stereotype.Component;

@Component
public class PacketWorker implements Runnable {

    private final FlowTracker flowTracker;

    public PacketWorker(FlowTracker flowTracker) {
        this.flowTracker = flowTracker;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Packet packet = PacketQueue.take();

                // Build FiveTuple
                FiveTuple tuple = new FiveTuple(
                        packet.srcIp,
                        packet.dstIp,
                        packet.srcPort,
                        packet.dstPort,
                        packet.protocol
                );

                // Create Flow (tuple MUST be stored in Flow class)
                Flow flow = new Flow(
                        tuple,
                        packet.size,
                        1
                );

                flowTracker.processPacket(flow);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}