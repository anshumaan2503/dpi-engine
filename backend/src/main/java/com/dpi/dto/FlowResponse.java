package com.dpi.dto;

import com.dpi.model.Flow;
import com.dpi.model.FiveTuple;

public class FlowResponse {

    public String srcIp;
    public String dstIp;
    public int srcPort;
    public int dstPort;
    public int protocol;

    public String app;
    public String domain;
    public boolean blocked;

    public long packets;
    public long bytes;
    public long duration;

    public FlowResponse(Flow flow) {
        FiveTuple key = flow.getKey();

        this.srcIp = key.getSrcIp();
        this.dstIp = key.getDstIp();
        this.srcPort = key.getSrcPort();
        this.dstPort = key.getDstPort();
        this.protocol = key.getProtocol();

        this.app = flow.getAppType();
        this.domain = flow.getDomain();
        this.blocked = flow.isBlocked();

        this.packets = flow.getPacketCount();
        this.bytes = flow.getByteCount();
        this.duration = flow.getDuration();
    }

}