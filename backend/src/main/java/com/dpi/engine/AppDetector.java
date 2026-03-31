package com.dpi.engine;

public class AppDetector {

    public String detect(String ip) {

        if (ip.startsWith("142.250") || ip.startsWith("142.251")) {
            return "Google";
        }

        if (ip.startsWith("140.82") || ip.startsWith("185.199")) {
            return "GitHub";
        }

        if (ip.startsWith("172.67") || ip.startsWith("104.26")) {
            return "Cloudflare";
        }

        if (ip.startsWith("20.")) {
            return "Microsoft";
        }

        if (ip.startsWith("216.239")) {
            return "Google CDN";
        }

        return "Unknown";
    }
}