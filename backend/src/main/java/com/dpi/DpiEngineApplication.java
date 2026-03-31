package com.dpi;

import com.dpi.engine.PcapReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DpiEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpiEngineApplication.class, args);
    }

    @Bean
    public CommandLineRunner startProcessing(PcapReader pcapReader) {
        return args -> {
            System.out.println("🚀 Starting packet processing...");

            // ⚠️ PUT YOUR ACTUAL PCAP FILE PATH HERE
            String filePath = "C:/temp/test.pcap";

            pcapReader.readFile(filePath);
        };
    }
}