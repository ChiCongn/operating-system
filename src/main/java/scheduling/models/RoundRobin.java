package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {
    //static int timeQuantum = 20; // default 20 ms

    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int[] currentTime, int timeQuantum) {
        System.out.println("Simulating Round Robin Scheduling");

        Queue<Process> readyQueue = new LinkedList<>();
        int totalProcesses = processes.size();
        //System.out.println("Total Processes: " + totalProcesses);

        double startX = 20;
        //int previousTime = currentTime;  // Track the last execution time for gaps

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int index = 0;

        // Add the first arriving process to the queue
        readyQueue.add(processes.get(index++));
        currentTime[0] = Math.max(processes.getFirst().arrivalTime, currentTime[0]);

        while (index < totalProcesses || !readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            if (currentProcess == null) {
                // If CPU is idle, draw an idle block
                int nextArrival = processes.get(index).arrivalTime;
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime[0], nextArrival - currentTime[0]);

                currentTime[0] = nextArrival;
                startX = currentTime[0] * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block

                // Add new processes that have arrived
                while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime[0]) {
                    readyQueue.add(processes.get(index++));
                }
                continue;
            }

            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime[0] - currentProcess.arrivalTime;
            }

            // Execute for either the time quantum or remaining time
            int executionTime = Math.min(timeQuantum, currentProcess.remainingTime);
            currentProcess.remainingTime -= executionTime;

            // Draw execution block
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.getName(), startX, currentTime[0], executionTime);
            currentTime[0] += executionTime;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime[0]) {
                readyQueue.add(processes.get(index++));
            }

            startX += executionTime * GanttChartDrawer.UNIT_WIDTH;

            // If process completes, update times
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime[0];
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                readyQueue.add(currentProcess);
            }
        }

        // Display total execution time on Gantt Chart
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }


    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime, int timeQuantum) {
        System.out.println("Simulating Round Robin Scheduling");

        Queue<Process> readyQueue = new LinkedList<>();
        int totalProcesses = processes.size();
        //System.out.println("Total Processes: " + totalProcesses);

        double startX = 20;
        //int previousTime = currentTime;  // Track the last execution time for gaps

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int index = 0;

        // Add the first arriving process to the queue
        readyQueue.add(processes.get(index++));
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        while (index < totalProcesses || !readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            if (currentProcess == null) {
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

            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // Execute for either the time quantum or remaining time
            int executionTime = Math.min(timeQuantum, currentProcess.remainingTime);
            currentProcess.remainingTime -= executionTime;

            // Draw execution block
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.getName(), startX, currentTime, executionTime);
            currentTime += executionTime;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            startX += executionTime * GanttChartDrawer.UNIT_WIDTH;

            // If process completes, update times
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                readyQueue.add(currentProcess);
            }
        }

        // Display total execution time on Gantt Chart
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }

}
