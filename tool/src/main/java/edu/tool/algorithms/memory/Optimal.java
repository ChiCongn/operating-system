package edu.tool.algorithms.memory;

import edu.tool.utils.Alert;
import edu.tool.utils.Drawer;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Optimal {

    public static void run(String[] references, int frameSize, Label pageFaults, GridPane grid) {
        System.out.println("Running Optimal...");

        int updateRow = -1, pageFaultCount = 0;
        int referenceCount = references.length;
        String[] referencesInFrame = new String[frameSize];
        Set<String> frameSet = new HashSet<>();

        Drawer.drawTheFirstRowGrid(references, grid);

        for (int i = 0; i < referenceCount; i++) {
            String currentPage = references[i];

            if (frameSet.contains(currentPage)) {
                Drawer.drawColumnGrid(referencesInFrame, i, -1, grid);
                continue;
            }


            if (frameSet.size() >= frameSize) {
                // Find the page to replace based on future use
                String pageToReplace = findOptimalReplacement(referencesInFrame, references, i + 1);
                updateRow = findReplaceRow(pageToReplace, referencesInFrame);
                frameSet.remove(pageToReplace);
            } else {
                updateRow++;
            }

            frameSet.add(currentPage);

            referencesInFrame[updateRow] = currentPage;
            Drawer.drawColumnGrid(referencesInFrame, i, updateRow, grid);

            pageFaultCount++;
        }

        pageFaults.setText(Integer.toString(pageFaultCount));
    }

    static int findReplaceRow(String replaceReference, String[] referencesInFrame) {
        for (int i = 0; i < referencesInFrame.length; i++) {
            if (replaceReference.equals(referencesInFrame[i])) {
                return i;
            }
        }
        return 0;
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
