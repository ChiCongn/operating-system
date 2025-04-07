package edu.tool.algorithms.memory;

import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.Set;

public class FIFO {

    public static void run(String[] references, int frameSize, Label pageFaults, GridPane grid) {
        System.out.println("Running FIFO...");

        int currentRow = 0, totalPageFaults = 0;
        int numberOfReferences = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> pagesInFrame = new HashSet<>();

        // draw the top row showing all page reference strings
        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < numberOfReferences; i++) {
            String currentReference = references[i];

            // if page is already in frame, just draw it (no page fault)
            if (!pagesInFrame.isEmpty() && pagesInFrame.contains(currentReference)) {
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }

            pagesInFrame.add(currentReference);

            // if frame is full, remove the oldest page
            if (pagesInFrame.size() > frameSize) {
                pagesInFrame.remove(referencesInFrame[currentRow]);
            }

            referencesInFrame[currentRow] = currentReference;
            Drawer.drawColumnGrid(referencesInFrame, i, currentRow, grid);

            totalPageFaults++;
            currentRow = (currentRow + 1) % frameSize;
        }

        // display total page faults
        pageFaults.setText(Integer.toString(totalPageFaults));
    }

}
