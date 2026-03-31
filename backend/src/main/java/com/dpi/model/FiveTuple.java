package com.dpi.model;

import java.util.Objects;

public class FiveTuple {

    private String srcIp;
    private String dstIp;
    private int srcPort;
    private int dstPort;
    private int protocol;

    public FiveTuple(String srcIp, String dstIp,
                     int srcPort, int dstPort,
                     int protocol) {

        String pair1 = srcIp + ":" + srcPort + "-" + dstIp + ":" + dstPort;
        String pair2 = dstIp + ":" + dstPort + "-" + srcIp + ":" + srcPort;

        if (pair1.compareTo(pair2) <= 0) {
            this.srcIp = srcIp;
            this.dstIp = dstIp;
            this.srcPort = srcPort;
            this.dstPort = dstPort;
        } else {
            this.srcIp = dstIp;
            this.dstIp = srcIp;
            this.srcPort = dstPort;
            this.dstPort = srcPort;
        }

        this.protocol = protocol;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public int getDstPort() {
        return dstPort;
    }

    public int getProtocol() {
        return protocol;
    }

    // VERY IMPORTANT — for HashMap key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FiveTuple)) return false;
        FiveTuple that = (FiveTuple) o;
        return srcPort == that.srcPort &&
                dstPort == that.dstPort &&
                protocol == that.protocol &&
                Objects.equals(srcIp, that.srcIp) &&
                Objects.equals(dstIp, that.dstIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcIp, dstIp, srcPort, dstPort, protocol);
    }
}