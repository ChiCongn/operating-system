package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class NonPreemptivePriority {

    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int[] currentTime) {
        System.out.println("Simulating Non-Preemptive Priority Scheduling");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        double startX = 20;
        int index = 0;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));

        while (index < processes.size() || !readyQueue.isEmpty()) {
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime[0]) {
                readyQueue.add(processes.get(index++));
            }

            // Handle CPU idle time
            if (readyQueue.isEmpty()) {
                if (index < processes.size()) {
                    int nextArrival = processes.get(index).arrivalTime;
                    GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime[0], nextArrival - currentTime[0]);
                    currentTime[0] = nextArrival;
                    startX = currentTime[0] * GanttChartDrawer.UNIT_WIDTH;
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime[0] - currentProcess.arrivalTime;
            }

            // Process execution calculations
            currentProcess.completionTime = currentTime[0] + currentProcess.burstTime;
            currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;

            // Draw process execution in the Gantt Chart
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.name, startX, currentTime[0], currentProcess.burstTime);

            // Update time tracking variables
            currentTime[0] += currentProcess.burstTime;
            startX += currentProcess.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);
    }

    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating Non-Preemptive Priority Scheduling");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        double startX = 20;
        int index = 0;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));

        while (index < processes.size() || !readyQueue.isEmpty()) {
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // Handle CPU idle time
            if (readyQueue.isEmpty()) {
                if (index < processes.size()) {
                    int nextArrival = processes.get(index).arrivalTime;
                    GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, nextArrival - currentTime);
                    currentTime = nextArrival;
                    startX = currentTime * GanttChartDrawer.UNIT_WIDTH;
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // Process execution calculations
            currentProcess.completionTime = currentTime + currentProcess.burstTime;
            currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;

            // Draw process execution in the Gantt Chart
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.name, startX, currentTime, currentProcess.burstTime);

            // Update time tracking variables
            currentTime += currentProcess.burstTime;
            startX += currentProcess.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
    }

}
