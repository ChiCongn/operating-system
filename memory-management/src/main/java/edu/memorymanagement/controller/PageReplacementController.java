package edu.memorymanagement.controller;

import edu.memorymanagement.PageReplacementAlgorithms;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.*;

public class PageReplacementController {

    @FXML
    GridPane grid;

    @FXML
    TextField frameSizeField;

    @FXML
    Label numberOfReferences;

    @FXML
    TextField referenceField;

    @FXML
    Label pageFaults;

    @FXML
    ChoiceBox<PageReplacementAlgorithms> pageReplacementAlgorithms;

    @FXML
    Button run;

    String[] references;
    int frameSize;

    @FXML
    void initialize() {
        pageReplacementAlgorithms.getItems().addAll(PageReplacementAlgorithms.values());
        run.setOnAction(event -> run());
    }

    void getInputValue() {
        String input = referenceField.getText();
        references = parseStringToStringArray(input);
        frameSize = Integer.parseInt(frameSizeField.getText());
        System.out.println("input: " + input);
        System.out.println("frame size: " + frameSize);
        numberOfReferences.setText(Integer.toString(frameSize));
    }

    void run() {
        // 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
        getInputValue();
        PageReplacementAlgorithms selectedAlgorithm = pageReplacementAlgorithms.getValue();
        runPageReplacementAlgorithm(selectedAlgorithm);
    }

    void runPageReplacementAlgorithm(PageReplacementAlgorithms algorithm) {
        switch (algorithm) {
            case FIFO:
                runFIFO();
                break;
            case LRU:
                runLRU();
                break;
            case OPTIMAL:
                runOptimal();
                break;
            case MRU:
                runMRU();
                break;
            case LFU:
                runLFU();
                break;
            case MFU:
                runMFU();
                break;
            case SECOND_CHANCE:
                runSecondChance();
                break;
            default:
                throw new UnsupportedOperationException("Unknown algorithm: " + algorithm);
        }
    }

    void runFIFO() {
        System.out.println("Running FIFO...");
        int currentRow = 0, totalPageFaults = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        drawTheFirstRow();

        for (int i = 0; i < numberOfReferences; i++) {
            if (!isInFrame.isEmpty() && isInFrame.contains(references[i])) {
                drawColumn(referencesInFrame, i, -1);
                continue;
            }

            isInFrame.add(references[i]);
            if (isInFrame.size() > frameSize) isInFrame.remove(referencesInFrame[currentRow]);

            referencesInFrame[currentRow] = references[i];
            drawColumn(referencesInFrame, i, currentRow);

            totalPageFaults++;
            currentRow++;
            currentRow %= frameSize;
        }
        pageFaults.setText(Integer.toString(totalPageFaults));
    }

    void runLRU() {
        System.out.println("Running LRU...");
        int currentRow = 0, totalPageFaults = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        LinkedList<String> accessOrder = new LinkedList<>();

        drawTheFirstRow();

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                // If the reference is already in the frame, we need to update its position in the accessOrder
                accessOrder.remove(references[i]);
                accessOrder.addFirst(references[i]);
                drawColumn(referencesInFrame, i, -1);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Remove the least recently used page from the frame (i.e., the last page in accessOrder)
                String leastRecentlyUsed = accessOrder.removeLast();
                isInFrame.remove(leastRecentlyUsed);
            }

            // Add the new reference to the frame and update the access order
            isInFrame.add(references[i]);
            accessOrder.addFirst(references[i]);

            referencesInFrame[currentRow] = references[i];
            drawColumn(referencesInFrame, i, currentRow);

            totalPageFaults++;
            currentRow++;
            currentRow %= frameSize;
        }

        pageFaults.setText(Integer.toString(totalPageFaults));
    }


    void runOptimal() {
        System.out.println("Running Optimal...");
        // Implement Optimal logic
    }

    void runMRU() {
        System.out.println("Running MRU...");
        int totalPageFaults = 0, currentRow = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        LinkedList<String> accessOrder = new LinkedList<>();

        drawTheFirstRow();

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                accessOrder.remove(references[i]);
                accessOrder.addFirst(references[i]); // Mark as most recently used
                drawColumn(referencesInFrame, i, -1);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Remove the most recently used page (i.e., first in accessOrder)
                String mostRecentlyUsed = accessOrder.removeFirst();
                isInFrame.remove(mostRecentlyUsed);
            }

            isInFrame.add(references[i]);
            accessOrder.addFirst(references[i]); // Add to the front as most recent

            referencesInFrame[currentRow] = references[i];
            drawColumn(referencesInFrame, i, currentRow);

            totalPageFaults++;
            currentRow++;
            currentRow %= frameSize;
        }

        pageFaults.setText(Integer.toString(totalPageFaults));
    }


    void runLFU() {
        System.out.println("Running LFU...");
        int totalPageFaults = 0, currentRow = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        Map<String, Integer> frequencyMap = new HashMap<>();

        drawTheFirstRow();

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                frequencyMap.put(references[i], frequencyMap.get(references[i]) + 1);
                drawColumn(referencesInFrame, i, -1);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Find the least frequently used page
                String leastFrequentlyUsed = Collections.min(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
                isInFrame.remove(leastFrequentlyUsed);
                frequencyMap.remove(leastFrequentlyUsed);
            }

            isInFrame.add(references[i]);
            frequencyMap.put(references[i], 1);

            referencesInFrame[currentRow] = references[i];
            drawColumn(referencesInFrame, i, currentRow);

            totalPageFaults++;
            currentRow++;
            currentRow %= frameSize;
        }

        pageFaults.setText(Integer.toString(totalPageFaults));
    }


    void runMFU() {
        System.out.println("Running MFU...");
        int totalPageFaults = 0, currentRow = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        Map<String, Integer> frequencyMap = new HashMap<>();

        drawTheFirstRow();

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                frequencyMap.put(references[i], frequencyMap.get(references[i]) + 1);
                drawColumn(referencesInFrame, i, -1);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Find the most frequently used page
                String mostFrequentlyUsed = Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
                isInFrame.remove(mostFrequentlyUsed);
                frequencyMap.remove(mostFrequentlyUsed);
            }

            isInFrame.add(references[i]);
            frequencyMap.put(references[i], 1);

            referencesInFrame[currentRow] = references[i];
            drawColumn(referencesInFrame, i, currentRow);

            totalPageFaults++;
            currentRow++;
            currentRow %= frameSize;
        }

        pageFaults.setText(Integer.toString(totalPageFaults));
    }


    void runSecondChance() {
        System.out.println("Running Second Chance...");
        // Implement Second Chance logic
    }

    void drawColumn(String[] reference, int index, int pageFaultIndex) {
        for (int i = 0; i < reference.length; i++) {
            Label label = new Label(reference[i]);
            label.getStyleClass().add("cell");

            if (i == pageFaultIndex) {
                label.getStyleClass().add("page-fault");
            } else if (reference[i] != null && !reference[i].isEmpty()) {
                label.getStyleClass().add("page-in-frame");
            } else {
                label.getStyleClass().add("empty-cell");
            }

            grid.add(label, index, i + 1);
        }
    }

    void drawTheFirstRow() {
        for (int i = 0; i < references.length; i++) {
            Label label = new Label(references[i]);
            label.getStyleClass().add("cell");
            label.getStyleClass().add("empty-cell");
            grid.add(label, i, 0);
        }
    }

    String[] parseStringToStringArray(String input) {
        input = input.trim();
        if (input.isEmpty()) return null;

        return input.split("\\s+");
    }
}
