package edu.tool.algorithms.memory;

import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MRU {

    public static void run(String[] references, int frameSize, Label pageFaults, GridPane grid) {
        System.out.println("Running MRU...");
        int totalPageFaults = 0, updateRow = -1;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        LinkedList<String> accessOrder = new LinkedList<>();

        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                accessOrder.remove(references[i]);
                accessOrder.addFirst(references[i]); // Mark as most recently used
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // Remove the most recently used page (i.e., first in accessOrder)
                String mostRecentlyUsed = accessOrder.removeFirst();
                updateRow = Optimal.findReplaceRow(mostRecentlyUsed, referencesInFrame);
                isInFrame.remove(mostRecentlyUsed);
            } else {
                updateRow++;
            }

            isInFrame.add(references[i]);
            accessOrder.addFirst(references[i]); // Add to the front as most recent

            referencesInFrame[updateRow] = references[i];
            Drawer.drawColumnGrid(referencesInFrame, i, updateRow, grid);

            totalPageFaults++;
        }

        // display total page faults
        pageFaults.setText(Integer.toString(totalPageFaults));
    }
}
