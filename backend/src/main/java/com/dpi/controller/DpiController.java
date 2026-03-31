package com.dpi.controller;

import com.dpi.dto.FlowResponse;
import com.dpi.dto.StatsResponse;
import com.dpi.dto.DomainStatsResponse;
import com.dpi.service.DpiService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dpi")
public class DpiController {

    private final DpiService dpiService;

    // ✅ Constructor Injection (NO new keyword)
    public DpiController(DpiService dpiService) {
        this.dpiService = dpiService;
    }

    @PostMapping("/read-pcap")
    public String readPcap(@RequestParam String filePath) {
        dpiService.readPcapFile(filePath);
        return "PCAP processing started";
    }

    @GetMapping("/flows")
    public List<FlowResponse> getFlows(
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) Boolean blocked,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return dpiService.getFlows(domain, blocked, page, size);
    }

    @GetMapping("/blocked")
    public List<FlowResponse> getBlockedFlows() {
        return dpiService.getBlockedFlows();
    }

    @GetMapping("/top")
    public List<FlowResponse> getTopFlows() {
        return dpiService.getTopFlows();
    }

    @DeleteMapping("/flows")
    public String clearFlows() {
        dpiService.clearFlows();
        return "Flows cleared";
    }

    @GetMapping("/stats")
    public StatsResponse getStats() {
        return dpiService.getStats();
    }

    @GetMapping("/top-domains")
    public List<DomainStatsResponse> getTopDomains() {
        return dpiService.getTopDomains();
    }
}