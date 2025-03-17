package edu.bankeranddeadlockdetector.models;

import edu.bankeranddeadlockdetector.utilities.Format;

public class Process {
    String processName;
    int[] allocation;
    int[] max;
    int[] need;
    int[] request;

    public Process(String processName, int[] allocation, int[] max) {
        this.processName = processName;
        this.allocation = allocation;
        this.max = max;
        this.need = calculateNeed();
        this.request = null;
    }

    public Process(String processName, int[] allocation, int[] request, boolean isDeadlockDetection) {
        this.processName = processName;
        this.allocation = allocation;
        this.request = request;
        this.max = null;   // Not needed in deadlock detection
        this.need = null;  // Not needed in deadlock detection
    }

    private int[] calculateNeed() {
        int[] need = new int[max.length];
        for (int i = 0; i < max.length; i++) {
            need[i] = max[i] - allocation[i];
        }
        return need;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int[] getAllocation() {
        return allocation;
    }

    public void setAllocation(int[] allocation) {
        this.allocation = allocation;
    }

    public int[] getMax() {
        return max;
    }

    public String getAllocationAsString() {
        return Format.formatArray(allocation); // Convert int[] → String
    }

    public void setAllocationFromString(String input) {
        this.allocation = Format.parseArray(input); // Convert String → int[]
    }

    public String getMaxAsString() {
        return Format.formatArray(max);
    }

    public void setMaxFromString(String input) {
        this.max = Format.parseArray(input);
    }

    public String getNeedAsString() {
        return Format.formatArray(need);
    }

    public void setNeedFromString(String input) {
        this.need = Format.parseArray(input);
    }

    public void setMax(int[] max) {
        this.max = max;
    }

    public int[] getNeed() {
        return need;
    }

    public void setNeed(int[] need) {
        this.need = need;
    }

    public int[] getRequest() {
        return request;
    }

    public String getRequestAsString() {
        return Format.formatArray(request);
    }

    public void setRequest(int[] request) {
        this.request = request;
    }
}

