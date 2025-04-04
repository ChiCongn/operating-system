package edu.tool.enums;

public enum SchedulingAlgorithm {
    FCFS("First Come First Serve"),
    PREEMPTIVE_SJF("Preemptive SJF"), // Shortest Job First Preemptive
    NON_PREEMPTIVE_SJF("Non-Preemptive SJF"),
    ROUND_ROBIN("Round Robin"),
    PREEMPTIVE_PRIORITY("Preemptive Priority Scheduling"),
    NON_PREEMPTIVE_PRIORITY("Non-Preemptive Priority Scheduling"),
    MULTI_LEVEL_QUEUE("Multi-Level Queue Scheduling"),
    MULTI_LEVEL_FEEDBACK_QUEUE("Multi-Level Feedback Queue");

    private final String displayName;

    SchedulingAlgorithm(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

