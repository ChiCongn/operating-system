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
        double startX = 20;

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int index = 0;

        // Add all processes that arrive at time 0
        while (index < totalProcesses && processes.get(index).arrivalTime == 0) {
            readyQueue.add(processes.get(index++));
        }

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            int executionTime = Math.min(timeQuantum, currentProcess.remainingTime);
            currentProcess.remainingTime -= executionTime;
            currentTime[0] += executionTime;

            // Draw Gantt Chart Block
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.name, startX, currentTime[0] - executionTime, executionTime);
            startX += executionTime * GanttChartDrawer.UNIT_WIDTH;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime[0]) {
                readyQueue.add(processes.get(index++));
            }

            // If the process is complete, set times
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime[0];
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                // Re-add to queue if not finished
                readyQueue.add(currentProcess);
            }
        }

        // Draw final time marker on Gantt Chart
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }


    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime, int timeQuantum) {
        System.out.println("Simulating Round Robin Scheduling");

        Queue<Process> readyQueue = new LinkedList<>();
        int totalProcesses = processes.size();
        double startX = 20;

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int index = 0;

        // Add all processes that arrive at time 0
        while (index < totalProcesses && processes.get(index).arrivalTime == 0) {
            readyQueue.add(processes.get(index++));
        }

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            int executionTime = Math.min(timeQuantum, currentProcess.remainingTime);
            currentProcess.remainingTime -= executionTime;
            currentTime += executionTime;

            // Draw Gantt Chart Block
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.name, startX, currentTime - executionTime, executionTime);
            startX += executionTime * GanttChartDrawer.UNIT_WIDTH;

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // If the process is complete, set times
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                // Re-add to queue if not finished
                readyQueue.add(currentProcess);
            }
        }

        // Draw final time marker on Gantt Chart
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }
}
