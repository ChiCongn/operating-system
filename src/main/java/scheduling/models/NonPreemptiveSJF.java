package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class NonPreemptiveSJF {
    public static void simulate(ObservableList<Process> processes, Canvas ganttChart) {
        System.out.println("Simulating Non-Preemptive Shortest Job First (SJF) Scheduling");

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        double startX = 20;
        int index = 0;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getRemainingTime));

        while (index < processes.size() || !readyQueue.isEmpty()) {
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // Handle CPU idle time
            if (readyQueue.isEmpty()) {
                if (index < processes.size()) {
                    int nextArrival = processes.get(index).getArrivalTime();
                    GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, nextArrival - currentTime);
                    currentTime = nextArrival;
                    startX = currentTime * GanttChartDrawer.UNIT_WIDTH;
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.getArrivalTime();
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


    /*public static void simulate(ObservableList<Process> processes, Canvas ganttChart) {
        System.out.println("simulating non preemptive scheduling");

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        double startX = 20;

        int index = 0; // Track position in processes list
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getBurstTime));

        while (index < processes.size() || !readyQueue.isEmpty()) {
            // Add all processes that have arrived at or before current time
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }


            if (readyQueue.isEmpty()) {
                // No process is ready, move forward in time
                currentTime++;
                continue;
            }

            // Select the process with the shortest burst time
            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // Execute the process
            currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());
            currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            startX = Math.max(startX, currentTime * GanttChartDrawer.UNIT_WIDTH);
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), currentProcess.getName(),
                    startX, currentTime, currentProcess.getBurstTime());

            currentTime += currentProcess.getBurstTime();
            startX += currentProcess.getBurstTime() * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);

    }*/
}
