package edu.tool.algorithms.memory;

import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class LFU {

    public static void run(String[] referenceSequence, int frameSize, Label pageFaultsLabel, GridPane gridPane) {
        System.out.println("Running LFU...");
        int updateRow = -1, totalPageFaults = 0;
        int totalReferences = referenceSequence.length;

        String[] frameSlots = new String[frameSize];
        Set<String> referencesInFrame = new HashSet<>();
        Map<String, Integer> pageFrequency = new HashMap<>();

        Drawer.drawTheFirstRowGrid(referenceSequence, gridPane);

        for (int i = 0; i < totalReferences; i++) {
            String currentPage = referenceSequence[i];

            // if the page is already in memory, just update its frequency
            if (referencesInFrame.contains(currentPage)) {
                pageFrequency.put(currentPage, pageFrequency.get(currentPage) + 1);
                Drawer.drawColumnGrid(frameSlots, i, -1, gridPane); // -1 indicates no page fault
                continue;
            }

            // if memory is full, remove the least frequently used page
            if (referencesInFrame.size() >= frameSize) {
                String leastFrequentPage = Collections
                        .min(pageFrequency.entrySet(), Map.Entry.comparingByValue())
                        .getKey();

                referencesInFrame.remove(leastFrequentPage);
                updateRow = Optimal.findReplaceRow(leastFrequentPage, frameSlots);
                pageFrequency.remove(leastFrequentPage);
            } else {
                updateRow++;
            }

            referencesInFrame.add(currentPage);
            pageFrequency.put(currentPage, 1);

            frameSlots[updateRow] = currentPage;
            Drawer.drawColumnGrid(frameSlots, i, updateRow, gridPane);

            totalPageFaults++;
        }

        // Display total number of page faults at the bottom
        pageFaultsLabel.setText(Integer.toString(totalPageFaults));
    }

}
