package edu.memorymanagement;

public enum MemoryUnit {
    BYTE("Byte"),
    KB("KB"),
    MB("MB"),
    GB("GB");

    private final String displayName;

    MemoryUnit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

