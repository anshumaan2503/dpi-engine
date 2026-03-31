package com.dpi.engine;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@Component
public class PcapReader {

    public void readFile(String filePath) {

        System.out.println("PATH RECEIVED: " + filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("❌ FILE NOT FOUND!");
            System.out.println("Absolute Path: " + file.getAbsolutePath());
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {

            System.out.println("✅ File opened successfully");

            byte[] globalHeader = new byte[24];
            if (fis.read(globalHeader) != 24) {
                System.out.println("❌ Invalid PCAP global header");
                return;
            }

            byte[] packetHeader = new byte[16];
            PacketParser parser = new PacketParser();

            while (true) {

                int headerRead = fis.read(packetHeader);
                if (headerRead != 16) break;

                int packetLength = getPacketLength(packetHeader);

                if (packetLength <= 0) continue;

                byte[] packetData = new byte[packetLength];
                int bytesRead = fis.read(packetData);

                if (bytesRead != packetLength) break;

                // ✅ Parse packet → Packet object
                Packet packet = parser.parsePacket(packetData);

                if (packet != null) {
                    PacketQueue.enqueue(packet); // async pipeline
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPacketLength(byte[] header) {
        return ((header[8] & 0xFF)) |
                ((header[9] & 0xFF) << 8) |
                ((header[10] & 0xFF) << 16) |
                ((header[11] & 0xFF) << 24);
    }
}