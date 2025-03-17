package edu.bankeranddeadlockdetector.models;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Banker {
    public static void simulateBanker(ObservableList<Process> processes, int[] available,
                                      StringBuilder executionDetail, StringBuilder safeProgress) {
        int numProcesses = processes.size();
        int numResources = available.length;

        boolean[] finished = new boolean[numProcesses];
        int[] work = available.clone();
        int count = 0;
        List<String> safeSequence = new ArrayList<>();

        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                Process process = processes.get(i);
                if (!finished[i]) {
                    int[] allocation = process.allocation;
                    int[] need = process.need;

                    boolean canAllocate = canExecute(numResources, need, work);

                    if (canAllocate) {
                        // Process i can finish, release resources
                        executionDetail.append("🔹 Checking Process ").append(process.getProcessName()).append(": Need ")
                                .append(Arrays.toString(need))
                                .append(" <= Available ")
                                .append(Arrays.toString(work)).append("\n✅ Need is satisfied. Process ")
                                .append(process.getProcessName()).append(" can execute.\n");

                        for (int j = 0; j < numResources; j++) {
                            work[j] += allocation[j];
                        }

                        executionDetail.append("🔄 Process ").append(process.getProcessName())
                                .append(" finishes. New Available Resources: ")
                                .append(Arrays.toString(work))
                                .append("\n---------------------------------\n");

                        safeSequence.add(process.processName);
                        finished[i] = true;
                        found = true;
                        count++;
                    }
                }
            }

            if (!found) {
                Alert.showNotification("System is in an unsafe state! Deadlock may occur!");
                safeProgress.append("System is in an unsafe state! Deadlock may occur!");
                return;
            }
        }

        //Alert.showNotification("System is in a safe state.\nSafe Sequence: " + safeSequence);
        safeProgress.append("Safe state! Safe Sequence: ");
        safeProgress.append(String.join(" --> ", safeSequence));

    }

    public static int[] calculateAvailableResources(ObservableList<Process> processes, int[] totalInstanceResources, int numOfResources) {
        int[] available = Arrays.copyOf(totalInstanceResources, numOfResources);
        System.out.println("available length " + available.length);

        for (Process process : processes) {
            int[] allocatedResources = process.getAllocation();
            for (int j = 0; j < numOfResources; j++) {
                available[j] -= allocatedResources[j];
            }
        }

        return available;
    }


    private static boolean canExecute(int numResources, int[] need, int[] work) {
        for (int j = 0; j < numResources; j++) {
            if (need[j] > work[j]) {
                return false;
            }
        }
        return true;
    }

}
