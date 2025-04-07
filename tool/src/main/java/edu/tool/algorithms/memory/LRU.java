package edu.tool.algorithms.memory;

import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class LRU {

    public static void run(String[] references, int frameSize, Label pageFaults, GridPane grid) {
        System.out.println("Running LRU...");
        int updateRow = -1, totalPageFaults = 0;
        int numberOfReferences = references.length;

        String[] referencesInFrame = new String[frameSize];
        Set<String> isInFrame = new HashSet<>();
        LinkedList<String> accessOrder = new LinkedList<>();

        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < numberOfReferences; i++) {
            if (isInFrame.contains(references[i])) {
                // if the reference is already in the frame, update its position in the accessOrder
                accessOrder.remove(references[i]);
                accessOrder.addFirst(references[i]);
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }

            if (isInFrame.size() >= frameSize) {
                // remove the least recently used page from the frame
                String leastRecentlyUsed = accessOrder.removeLast();
                updateRow = Optimal.findReplaceRow(leastRecentlyUsed, referencesInFrame);
                isInFrame.remove(leastRecentlyUsed);
            } else {
                updateRow++;
            }

            // add the new reference to the frame and update the access order
            isInFrame.add(references[i]);
            accessOrder.addFirst(references[i]);

            referencesInFrame[updateRow] = references[i];
            Drawer.drawColumnGrid(referencesInFrame, i, updateRow, grid);

            totalPageFaults++;
        }

        // display total page faults
        pageFaults.setText(Integer.toString(totalPageFaults));
    }
}
