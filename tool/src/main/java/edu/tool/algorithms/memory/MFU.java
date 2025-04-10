package edu.tool.algorithms.memory;

import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class MFU {

    public static void run(String[] references, int frameSize, Label pageFaults, GridPane grid) {
        System.out.println("Running MFU...");
        int totalPageFaults = 0, updateRow = -1;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        Map<String, Integer> frequencyMap = new HashMap<>();

        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                frequencyMap.put(references[i], frequencyMap.get(references[i]) + 1);
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Find the most frequently used page
                String mostFrequentlyUsed = Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
                isInFrame.remove(mostFrequentlyUsed);
                updateRow = Optimal.findReplaceRow(mostFrequentlyUsed, referencesInFrame);
                frequencyMap.remove(mostFrequentlyUsed);
            } else {
                updateRow++;
            }

            isInFrame.add(references[i]);
            frequencyMap.put(references[i], 1);

            referencesInFrame[updateRow] = references[i];
            Drawer.drawColumnGrid(referencesInFrame, i, updateRow, grid);

            totalPageFaults++;
        }

        // display total page faults
        pageFaults.setText(Integer.toString(totalPageFaults));
    }
}
