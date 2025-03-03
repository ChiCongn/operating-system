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
        currentTime[0] = Math.max(processes.getFirst().arrivalTime, currentTime[0]);

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
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        double startX = 20;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));

        Process currentProcess = null;
        Process lastProcess = null;
        int lastProcessStartTime = currentTime;
        int processIndex = 0; // Track next process to enter the queue

        while (completedProcess < processes.size()) {
            while (processIndex < processes.size() && processes.get(processIndex).arrivalTime == currentTime) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            // If no process is ready, move time forward and draw an idle block
            if (readyQueue.isEmpty()) {
                int nextArrival = (processIndex < processes.size()) ? processes.get(processIndex).arrivalTime : currentTime;
                if (currentTime < nextArrival) {
                    int idleDuration = nextArrival - currentTime;
                    GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, idleDuration);
                    startX += idleDuration * GanttChartDrawer.UNIT_WIDTH;
                    currentTime = nextArrival;
                }
                continue;
            }

            // If a process is running and not finished, re-add it to the queue
            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // If process changes, update the Gantt Chart
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

            currentTime++;
        }

        // Draw the last segment in the Gantt Chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime);
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
        return completedProcess;
    }


}
