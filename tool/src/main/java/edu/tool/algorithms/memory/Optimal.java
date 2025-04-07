package edu.tool.algorithms.memory;

import edu.tool.utils.Alert;
import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Optimal {

    public static void run(String[] references, int frameCapacity, Label pageFaults, GridPane grid) {
        System.out.println("Running Optimal...");

        int currentRow = 0, pageFaultCount = 0;
        int referenceCount = references.length;
        String[] referencesInFrame = new String[frameCapacity];
        Set<String> frameSet = new HashSet<>();

        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < referenceCount; i++) {
            String currentPage = references[i];

            if (frameSet.contains(currentPage)) {
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }

            if (frameSet.size() >= frameCapacity) {
                // Find the page to replace based on future use
                String pageToReplace = findOptimalReplacement(referencesInFrame, references, i + 1);
                frameSet.remove(pageToReplace);
            }

            frameSet.add(currentPage);
            referencesInFrame[currentRow] = currentPage;
            Drawer.drawColumnGrid(referencesInFrame, i, currentRow, grid);

            pageFaultCount++;
            currentRow = (currentRow + 1) % frameCapacity;
        }

        pageFaults.setText(Integer.toString(pageFaultCount));
    }


    private static String findOptimalReplacement(String[] frames, String[] references, int startIndex) {
        int farthest = -1;
        int replaceIndex = -1;

        for (int i = 0; i < frames.length; i++) {
            String page = frames[i];
            boolean found = false;

            for (int j = startIndex; j < references.length; j++) {
                if (page.equals(references[j])) {
                    found = true;
                    if (j > farthest) {
                        farthest = j;
                        replaceIndex = i;
                    }
                    break;
                }
            }

            if (!found) {
                return page;
            }
        }

        return frames[replaceIndex != -1 ? replaceIndex : 0];
    }


}
