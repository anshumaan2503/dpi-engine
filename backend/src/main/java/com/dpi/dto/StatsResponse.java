package com.dpi.dto;

public class StatsResponse {

    public long totalFlows;
    public long blockedFlows;
    public long totalPackets;
    public long totalBytes;

    public StatsResponse(long totalFlows, long blockedFlows, long totalPackets, long totalBytes) {
        this.totalFlows = totalFlows;
        this.blockedFlows = blockedFlows;
        this.totalPackets = totalPackets;
        this.totalBytes = totalBytes;
    }
}