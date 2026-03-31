package com.dpi.engine;

public class Packet {

    public String srcIp;
    public String dstIp;
    public int srcPort;
    public int dstPort;
    public  int protocol;

    public String domain; // from TLS SNI (optional)
    public long size;

    public Packet(String srcIp, String dstIp,
                  int srcPort, int dstPort,
                  int protocol,   // FIXED
                  String domain,
                  long size) {

        this.srcIp = srcIp;
        this.dstIp = dstIp;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.protocol = protocol;
        this.domain = domain;
        this.size = size;
    }
}