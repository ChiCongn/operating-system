package edu.bankeranddeadlockdetector.utilities;

import javafx.collections.ObservableList;
import edu.bankeranddeadlockdetector.models.Process;

public class Validation {

    // check if allocation is less than or equal to max
    public static boolean isValidAllocation(int[] allocation, int[] max) {
        if (allocation.length != max.length) return false;
        for (int i = 0; i < allocation.length; i++) {
            if (allocation[i] > max[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // Check if a number is a natural number
    public static boolean isNaturalNumber(int number) {
        return number >= 0;
    }

    // Validate an array of resource instances (all must be natural numbers)
    public static boolean isNaturalNumberArray(int[] array) {
        for (int resource : array) {
            if (resource < 0) {
                return false;
            }
        }
        return true;
    }

    // Check if total allocation for each resource is within available instances
    public static boolean isValidTotalAllocation(ObservableList<Process> processes, int[] totalInstances, int numOfResources) {
        int[] totalAllocation = new int[numOfResources];

        // Sum allocations for each resource
        for (Process process : processes) {
            int[] allocation = process.getAllocation();
            for (int i = 0; i < numOfResources; i++) {
                totalAllocation[i] += allocation[i];
            }
        }

        // Check if allocation exceeds available instances
        for (int i = 0; i < numOfResources; i++) {
            if (totalAllocation[i] > totalInstances[i]) {
                return false;
            }
        }
        return true;
    }
}
