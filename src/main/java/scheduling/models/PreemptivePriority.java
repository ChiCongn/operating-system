package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptivePriority {
    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Running Preemptive Priority Scheduling...");
        
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int completedProcess = 0;
        double startX = 20;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt((Process p) -> p.priority).thenComparingInt(p -> p.arrivalTime)
        );

        Process currentProcess = null, lastProcess = null;
        int lastProcessStartTime = 0;
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the queue
            for (Process p : processes) {
                if (p.arrivalTime == currentTime) {
                    readyQueue.add(p);
                }
            }

            // Re-add running process if it's not finished
            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            // Select the highest-priority process
            if (!readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                if (currentProcess.responseTime == -1) {
                    currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
                }

                // If process changes, draw the previous process in Gantt Chart
                if (lastProcess != null && lastProcess != currentProcess) {
                    startX = Math.max(startX, currentTime * GanttChartDrawer.UNIT_WIDTH);
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.getName(), startX, lastProcessStartTime, currentTime - lastProcessStartTime);

                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                    lastProcessStartTime = currentTime;
                }
                lastProcess = currentProcess;

                // Execute process for 1 time unit
                currentProcess.remainingTime--;

                // If process completes, set final times
                if (currentProcess.remainingTime == 0) {
                    completedProcess++;
                    currentProcess.completionTime = currentTime + 1;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                }
            }

            currentTime++;
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    lastProcess.getName(), startX, lastProcessStartTime, currentTime - lastProcessStartTime);
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
        return completedProcess;
    }

}
