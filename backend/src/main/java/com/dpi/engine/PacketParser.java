package com.dpi.engine;

public class PacketParser {

    public Packet parsePacket(byte[] data) {

        try {
            // ================= Ethernet (skip 14 bytes) =================
            int offset = 14;

            // ================= IP Header =================
            int versionAndHeaderLength = data[offset] & 0xFF;
            int ipHeaderLength = (versionAndHeaderLength & 0x0F) * 4;

            int protocol = data[offset + 9] & 0xFF;

            String srcIp = (data[offset + 12] & 0xFF) + "." +
                    (data[offset + 13] & 0xFF) + "." +
                    (data[offset + 14] & 0xFF) + "." +
                    (data[offset + 15] & 0xFF);

            String dstIp = (data[offset + 16] & 0xFF) + "." +
                    (data[offset + 17] & 0xFF) + "." +
                    (data[offset + 18] & 0xFF) + "." +
                    (data[offset + 19] & 0xFF);

            offset += ipHeaderLength;

            int srcPort = 0;
            int dstPort = 0;

            // ================= TCP =================
            if (protocol == 6 && data.length >= offset + 4) {
                srcPort = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
                dstPort = ((data[offset + 2] & 0xFF) << 8) | (data[offset + 3] & 0xFF);
            }

            // ================= UDP =================
            else if (protocol == 17 && data.length >= offset + 4) {
                srcPort = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
                dstPort = ((data[offset + 2] & 0xFF) << 8) | (data[offset + 3] & 0xFF);
            }

            // ================= TLS SNI =================
            String domain = null;

            if (protocol == 6 && dstPort == 443) {
                domain = extractSNI(data, offset);
            }

            // ================= SIZE =================
            long size = data.length; // ✅ FIXED

            // ================= RETURN PACKET =================
            return new Packet(srcIp, dstIp, srcPort, dstPort, protocol, domain, size);

        } catch (Exception e) {
            return null; // safe fail
        }
    }

    // ================= TLS SNI Extraction =================
    private String extractSNI(byte[] data, int offset) {

        try {
            int pos = offset + 5;

            if (pos >= data.length) return null;

            int sessionIdLength = data[pos] & 0xFF;
            pos += 1 + sessionIdLength;

            if (pos + 2 > data.length) return null;

            int cipherLength = ((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF);
            pos += 2 + cipherLength;

            if (pos >= data.length) return null;

            int compressionLength = data[pos] & 0xFF;
            pos += 1 + compressionLength;

            if (pos + 2 > data.length) return null;

            int extensionsLength = ((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF);
            pos += 2;

            int end = pos + extensionsLength;

            while (pos + 4 <= end && pos + 4 <= data.length) {

                int type = ((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF);
                int length = ((data[pos + 2] & 0xFF) << 8) | (data[pos + 3] & 0xFF);
                pos += 4;

                if (type == 0x00) {
                    pos += 2;

                    if (pos + 3 > data.length) return null;

                    int nameLength = ((data[pos + 1] & 0xFF) << 8) | (data[pos + 2] & 0xFF);
                    pos += 3;

                    if (pos + nameLength > data.length) return null;

                    return new String(data, pos, nameLength);
                }

                pos += length;
            }

        } catch (Exception ignored) {}

        return null;
    }
}