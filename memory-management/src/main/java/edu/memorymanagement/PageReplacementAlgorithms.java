package edu.memorymanagement;

public enum PageReplacementAlgorithms {
    FIFO("First-In-First-Out"),
    OPTIMAL("Optimal"),
    LRU("Least Recently Used"),
    MRU("Most Recently Used"),
    LFU("Least Frequently Used"),
    MFU("Most Frequently Used"),
    SECOND_CHANCE("Second Chance");

    private final String description;

    PageReplacementAlgorithms(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PageReplacementAlgorithms getAlgorithm(PageReplacementAlgorithms algorithm) {
        return switch (algorithm) {
            case FIFO -> FIFO;
            case OPTIMAL -> OPTIMAL;
            case LRU -> LRU;
            case MRU -> MRU;
            case LFU -> LFU;
            case MFU -> MFU;
            case SECOND_CHANCE -> SECOND_CHANCE;
            default -> throw new UnsupportedOperationException("Algorithm not supported.");
        };
    }
}
