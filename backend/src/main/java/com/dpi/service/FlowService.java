package com.dpi.service;

import com.dpi.model.Flow;
import com.dpi.store.FlowStore;

import java.util.List;

public class FlowService {

    private final FlowStore store = FlowStore.getInstance();

    public List<Flow> getAllFlows() {
        return store.getAllFlows()
                .stream()
                .toList();
    }

    public void clearFlows() {
        store.clear();
    }
}