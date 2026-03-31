package com.dpi.store;

import com.dpi.model.Flow;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncFlowQueue {

    private static final int QUEUE_CAPACITY = 10000;

    private static final BlockingQueue<Flow> queue =
            new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    public static void enqueue(Flow flow) {
        try {
            queue.put(flow); // blocks if full (backpressure)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static Flow take() throws InterruptedException {
        return queue.take(); // blocking consume
    }

    public static int size() {
        return queue.size();
    }
}