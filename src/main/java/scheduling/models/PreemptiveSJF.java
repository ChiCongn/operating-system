package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptiveSJF {
    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int[] currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        double startX = 20;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));

        Process currentProcess = null;
        Process lastProcess = null;
        int lastProcessStartTime = 0;
        int processIndex = 0; // Track the next process to arrive

        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the ready queue
            while (processIndex < processes.size() && processes.get(processIndex).arrivalTime == currentTime[0]) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            // Re-add the running process if it's not finished
            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            // Pick the process with the shortest remaining time
            if (!readyQueue.isEmpty()) {
                Process nextProcess = readyQueue.poll();

                // If process changes, update Gantt chart
                if (currentProcess != nextProcess) {
                    if (lastProcess != null) {
                        GanttChartDrawer.drawColumn(
                                ganttChart.getGraphicsContext2D(),
                                lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
                        );
                        startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                    }
                    lastProcessStartTime = currentTime[0];
                }

                currentProcess = nextProcess;
                lastProcess = currentProcess;

                // Execute process for 1 time unit
                currentProcess.remainingTime--;

                // If process is finished
                if (currentProcess.remainingTime == 0) {
                    completedProcess++;
                    currentProcess.completionTime = currentTime[0] + 1;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                }
            }

            currentTime[0]++;
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(
                    ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
            );
            startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display final time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);

        return completedProcess;
    }


    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        double startX = 20;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));

        Process currentProcess = null;
        Process lastProcess = null; // Track Gantt Chart segments
        int lastProcessStartTime = 0;
        int processIndex = 0; // Track next process to enter the queue

        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the ready queue
            while (processIndex < processes.size() && processes.get(processIndex).arrivalTime == currentTime) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            // If a process is running and not finished, re-add to the queue
            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            // Pick the process with the shortest remaining time
            if (!readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();

                // If process changes, update Gantt Chart
                if (lastProcess != null && lastProcess != currentProcess) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime);
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                    lastProcessStartTime = currentTime;
                }
                lastProcess = currentProcess;

                // Execute process for 1 time unit
                currentProcess.remainingTime--;

                // If the process is completed
                if (currentProcess.remainingTime == 0) {
                    completedProcess++;
                    currentProcess.completionTime = currentTime + 1;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                }
            }

            currentTime++;
        }

        // Draw the last segment in the Gantt Chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime);
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
        return completedProcess;
    }

}
