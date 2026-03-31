package com.dpi.store;

import com.dpi.model.Flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FlowStore {

    private static final FlowStore INSTANCE = new FlowStore();

    private final Map<String, Flow> flows = new HashMap<>();

    private FlowStore() {}

    public static FlowStore getInstance() {
        return INSTANCE;
    }

    public void addFlow(String key, Flow flow) {
        flows.put(key, flow);
    }

    public Collection<Flow> getAllFlows() {
        return flows.values();
    }

    public void clear() {
        flows.clear();
    }
}