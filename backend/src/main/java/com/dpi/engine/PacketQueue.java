package com.dpi.engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketQueue {

    private static final BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    public static void enqueue(Packet packet) {
        queue.offer(packet);
    }

    public static Packet take() throws InterruptedException {
        return queue.take();
    }
}