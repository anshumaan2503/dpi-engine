package com.dpi.dto;

public class DomainStatsResponse {

    public String domain;
    public long bytes;

    public DomainStatsResponse(String domain, long bytes) {
        this.domain = domain;
        this.bytes = bytes;
    }
}