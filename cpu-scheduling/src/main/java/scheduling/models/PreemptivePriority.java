package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptivePriority {
    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int currentTime) {
        System.out.println("Simulating Preemptive Priority");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        int index = 0, totalProcesses = processes.size();
        double startX = 20;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));
        readyQueue.add(processes.get(index++));

        Process lastProcess = readyQueue.peek();
        int lastProcessStartTime = currentTime;

        while (completedProcess < totalProcesses) {
            if (readyQueue.isEmpty()) {
                // draw previous block before drawing idle time
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
                    );
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime;
                lastProcess = null;

                // If CPU is idle, draw an idle block
                int nextArrival = processes.get(index).arrivalTime;

                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, nextArrival - currentTime);

                currentTime = nextArrival;
                startX = currentTime * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block

                // Add new processes that have arrived
                while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                    readyQueue.add(processes.get(index++));
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // If process changes, update Gantt chart
            if (currentProcess != lastProcess) {
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
                    );
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime;
                lastProcess = currentProcess;
            }

            // Execute process for 1 time unit quantum
            currentTime++;
            currentProcess.remainingTime--;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // If process finishes execution
            if (currentProcess.remainingTime == 0) {
                completedProcess++;
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                readyQueue.add(currentProcess); // Re-add the process if it's not finished
            }
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(
                    ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
            );
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);

        return completedProcess;
    }

    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int[] currentTime) {
        System.out.println("Simulating Preemptive Priority");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        currentTime[0] = Math.max(processes.getFirst().arrivalTime, currentTime[0]);

        int index = 0, totalProcesses = processes.size();
        double startX = 20;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));
        readyQueue.add(processes.get(index++));

        Process lastProcess = readyQueue.peek();
        int lastProcessStartTime = currentTime[0];

        while (completedProcess < totalProcesses) {
            if (readyQueue.isEmpty()) {
                // draw previous block before drawing idle time
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
                    );
                    startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime[0];
                lastProcess = null;

                // If CPU is idle, draw an idle block
                int nextArrival = processes.get(index).arrivalTime;

                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime[0],
                        nextArrival - currentTime[0]);

                currentTime[0] = nextArrival;
                startX = currentTime[0] * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block

                // Add new processes that have arrived
                while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime[0]) {
                    readyQueue.add(processes.get(index++));
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime[0] - currentProcess.arrivalTime;
            }

            // If process changes, update Gantt chart
            if (currentProcess != lastProcess) {
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
                    );
                    startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime[0];
                lastProcess = currentProcess;
            }

            // Execute process for 1 time unit quantum
            currentTime[0]++;
            currentProcess.remainingTime--;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime[0]) {
                readyQueue.add(processes.get(index++));
            }

            // If process finishes execution
            if (currentProcess.remainingTime == 0) {
                completedProcess++;
                currentProcess.completionTime = currentTime[0];
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                readyQueue.add(currentProcess); // Re-add the process if it's not finished
            }
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(
                    ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
            );
            startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);

        return completedProcess;
    }

}
