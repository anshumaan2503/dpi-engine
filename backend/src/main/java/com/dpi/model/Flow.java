package com.dpi.model;

public class Flow {

    private String sni;
    private String appType;
    private boolean blocked;
    private String domain;

    // ✅ SINGLE source of truth
    private final FiveTuple key;

    private long packetCount;
    private long byteCount;

    private long startTime;
    private long lastSeenTime;

    public Flow(FiveTuple key, long timestamp, int packetSize) {
        this.key = key;

        this.appType = "UNKNOWN";
        this.blocked = false;

        this.startTime = timestamp;
        this.lastSeenTime = timestamp;

        this.packetCount = 1;
        this.byteCount = packetSize;
    }

    public void update(long timestamp, int packetSize) {
        this.packetCount++;
        this.byteCount += packetSize;
        this.lastSeenTime = timestamp;
    }

    public long getDuration() {
        return lastSeenTime - startTime;
    }

    // ✅ USE THIS EVERYWHERE
    public FiveTuple getKey() {
        return key;
    }

    // 🔥 CRITICAL FIX — redirect old calls
    public FiveTuple getFiveTuple() {
        return key; // instead of NULL
    }

    // ---------------- GETTERS / SETTERS ----------------

    public String getSni() {
        return sni;
    }

    public void setSni(String sni) {
        this.sni = sni;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }
}