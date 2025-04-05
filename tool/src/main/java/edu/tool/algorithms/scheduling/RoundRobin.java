package edu.tool.algorithms.scheduling;

import edu.tool.models.ScheduledProcess;
import edu.tool.utils.GanttChartDrawer;
import javafx.scene.canvas.Canvas;

import java.util.*;

public class RoundRobin {
    public static void simulate(List<ScheduledProcess> processes, Canvas ganttChart, int timeQuantum, int currentTime) {
        System.out.println("Simulating Round Robin Scheduling");
        int totalProcesses = processes.size(), index = 0, completed = 0;
        double startX = GanttChartDrawer.START_X;

        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime));

        // Add the first arriving process to the queue
        Queue<ScheduledProcess> readyQueue = new LinkedList<>();
        readyQueue.add(processes.get(index++));
        currentTime = Math.max(processes.getFirst().getArrivalTime(), currentTime);

        while (completed < totalProcesses) {
            if (readyQueue.isEmpty()) {
                ScheduledProcess nextProcess = processes.get(index++);
                readyQueue.add(nextProcess);
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime);

                currentTime = nextProcess.getArrivalTime();
                startX += GanttChartDrawer.UNIT_WIDTH;
                continue;
            }

            ScheduledProcess currentProcess = readyQueue.poll();
            handleResponseTime(currentTime, currentProcess);

            // Execute for either the time quantum or remaining time
            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            currentProcess.decrementRemainingTime(executionTime);

            GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), currentProcess.getName(), startX, currentTime);
            startX += GanttChartDrawer.UNIT_WIDTH;
            currentTime += executionTime;

            // add newly arrived processes
            while (index < totalProcesses && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            completed = completeProcessIfCan(currentTime, currentProcess, completed, readyQueue);
        }

        // Display the final time after all processes are executed
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime),
                startX, GanttChartDrawer.POSITION_Y + 50);
    }

    public static int simulateAndUpdateTime(List<ScheduledProcess> processes, Canvas ganttChart, int timeQuantum, int currentTime) {
        System.out.println("Simulating Round Robin Scheduling");
        int totalProcesses = processes.size(), index = 0, completed = 0;
        double startX = GanttChartDrawer.START_X;

        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime));

        // Add the first arriving process to the queue
        Queue<ScheduledProcess> readyQueue = new LinkedList<>();
        readyQueue.add(processes.get(index++));
        currentTime = Math.max(processes.getFirst().getArrivalTime(), currentTime);

        while (completed < totalProcesses) {
            if (readyQueue.isEmpty()) {
                ScheduledProcess nextProcess = processes.get(index++);
                readyQueue.add(nextProcess);
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime);

                currentTime = nextProcess.getArrivalTime();
                startX += GanttChartDrawer.UNIT_WIDTH;
                continue;
            }

            ScheduledProcess currentProcess = readyQueue.poll();
            handleResponseTime(currentTime, currentProcess);

            // Execute for either the time quantum or remaining time
            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            currentProcess.decrementRemainingTime(executionTime);

            GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), currentProcess.getName(), startX, currentTime);
            startX += GanttChartDrawer.UNIT_WIDTH;
            currentTime += executionTime;

            // add newly arrived processes
            while (index < totalProcesses && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            completed = completeProcessIfCan(currentTime, currentProcess, completed, readyQueue);
        }

        // Display the final time after all processes are executed
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime),
                startX, GanttChartDrawer.POSITION_Y + 50);
        return currentTime;
    }

    static int completeProcessIfCan(int currentTime, ScheduledProcess current, int completed,
                                    Queue<ScheduledProcess> readyQueue) {
        if (current.getRemainingTime() == 0) {
            completed++;
            current.setFinishTime(currentTime);
            current.setTurnaroundTime(currentTime - current.getArrivalTime());
            current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
        } else {
            readyQueue.add(current);
        }
        return completed;
    }

    static void handleResponseTime(int currentTime, ScheduledProcess current) {
        if (current.getResponseTime() == -1) {
            current.setResponseTime(currentTime - current.getArrivalTime());
        }
        current.decrementRemainingTime(1);
    }
}
