package edu.tool.algorithms.scheduling;

import edu.tool.models.ScheduledProcess;
import edu.tool.utils.GanttChartDrawer;
import javafx.scene.canvas.Canvas;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SJF {
    public static int simulatePreemptiveAndUpdateTime(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");
        double startX = GanttChartDrawer.START_X;
        int totalProcesses = processes.size(), completed = 0, index = 0;

        // Sort initially by arrival time, then priority
        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getPriority));

        currentTime = Math.max(currentTime, processes.getFirst().getArrivalTime());
        int lastStartTime = currentTime;

        PriorityQueue<ScheduledProcess> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(ScheduledProcess::getRemainingTime));
        readyQueue.add(processes.get(index++));
        ScheduledProcess lastProcess = readyQueue.peek();

        while (completed < totalProcesses) {
            if (readyQueue.isEmpty()) {
                ScheduledProcess nextProcess = processes.get(index++);
                readyQueue.add(nextProcess);

                currentTime = handleIdleAndUpdateTime(ganttChart, currentTime, nextProcess, startX);
                startX += GanttChartDrawer.UNIT_WIDTH;
                lastProcess = null;
                lastStartTime = currentTime;
                continue;
            }

            ScheduledProcess current = readyQueue.poll();
            handleResponseTime(currentTime, current);

            if (current != lastProcess && lastProcess != null) {
                GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), lastProcess.getName(), startX, lastStartTime);
                startX += GanttChartDrawer.UNIT_WIDTH;
                lastProcess = current;
                lastStartTime = currentTime;
            }
            currentTime++; // run the process for one unit of time
            completed = completeProcessIfCan(currentTime, current, completed, readyQueue);

            // add newly arrived processes
            while (index < totalProcesses && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }
        }

        drawLastProcessAndFinalTime(ganttChart, currentTime, lastProcess, startX, lastStartTime);
        return currentTime;
    }

    public static void simulatePreemptive(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");
        double startX = GanttChartDrawer.START_X;
        int totalProcesses = processes.size(), completed = 0, index = 0;

        // Sort initially by arrival time, then priority
        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getPriority));

        currentTime = Math.max(currentTime, processes.getFirst().getArrivalTime());
        int lastStartTime = currentTime;

        PriorityQueue<ScheduledProcess> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(ScheduledProcess::getRemainingTime));
        readyQueue.add(processes.get(index++));
        ScheduledProcess lastProcess = readyQueue.peek();

        while (completed < totalProcesses) {
            if (readyQueue.isEmpty()) {
                ScheduledProcess nextProcess = processes.get(index++);
                readyQueue.add(nextProcess);

                currentTime = handleIdleAndUpdateTime(ganttChart, currentTime, nextProcess, startX);
                startX += GanttChartDrawer.UNIT_WIDTH;
                lastProcess = null;
                lastStartTime = currentTime;
                continue;
            }

            ScheduledProcess current = readyQueue.poll();
            handleResponseTime(currentTime, current);

            if (current != lastProcess && lastProcess != null) {
                GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), lastProcess.getName(), startX, lastStartTime);
                startX += GanttChartDrawer.UNIT_WIDTH;
                lastProcess = current;
                lastStartTime = currentTime;
            }
            currentTime++; // run the process for one unit of time
            completed = completeProcessIfCan(currentTime, current, completed, readyQueue);

            // add newly arrived processes
            while (index < totalProcesses && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }
        }

        drawLastProcessAndFinalTime(ganttChart, currentTime, lastProcess, startX, lastStartTime);
    }

    public static void simulateNonPreemptive(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating Non Preemptive Shortest Job First (SJF)");
        double startX = GanttChartDrawer.START_X;
        int totalProcesses = processes.size(), completed = 0, index = 0;

        // Sort the processes first by arrival time, then by remaining time, and finally by priority
        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getRemainingTime).thenComparingInt(ScheduledProcess::getPriority));

        currentTime = Math.max(currentTime, processes.getFirst().getArrivalTime());

        for (ScheduledProcess process : processes) {
            // If there's idle time (gap) between the current time and the process arrival time, handle idle time
            if (currentTime < process.getArrivalTime()) {
                currentTime = handleIdleAndUpdateTime(ganttChart, currentTime, process, startX);
                startX += GanttChartDrawer.UNIT_WIDTH;
            }

            // Execute the process and update the time accordingly
            currentTime = executeProcessAndUpdateTime(ganttChart, currentTime, process, startX);
            startX += GanttChartDrawer.UNIT_WIDTH;
        }

        // Display the final time after all processes are executed
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime),
                startX, GanttChartDrawer.POSITION_Y + 50);
    }

    public static int simulateNonPreemptiveAndUpdateTime(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating Non Preemptive Shortest Job First (SJF)");
        double startX = GanttChartDrawer.START_X;
        int totalProcesses = processes.size(), completed = 0, index = 0;

        // Sort the processes first by arrival time, then by remaining time, and finally by priority
        processes.sort(Comparator.comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getRemainingTime).thenComparingInt(ScheduledProcess::getPriority));

        currentTime = Math.max(currentTime, processes.getFirst().getArrivalTime());

        for (ScheduledProcess process : processes) {
            // If there's idle time (gap) between the current time and the process arrival time, handle idle time
            if (currentTime < process.getArrivalTime()) {
                currentTime = handleIdleAndUpdateTime(ganttChart, currentTime, process, startX);
                startX += GanttChartDrawer.UNIT_WIDTH;
            }

            // Execute the process and update the time accordingly
            currentTime = executeProcessAndUpdateTime(ganttChart, currentTime, process, startX);
            startX += GanttChartDrawer.UNIT_WIDTH;
        }

        // Display the final time after all processes are executed
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime),
                startX, GanttChartDrawer.POSITION_Y + 50);
        return currentTime;
    }

    static void handleResponseTime(int currentTime, ScheduledProcess current) {
        if (current.getResponseTime() == -1) {
            current.setResponseTime(currentTime - current.getArrivalTime());
        }
        current.decrementRemainingTime(1);
    }

    static int completeProcessIfCan(int currentTime, ScheduledProcess current, int completed,
                                    PriorityQueue<ScheduledProcess> readyQueue) {
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

    static void drawLastProcessAndFinalTime(Canvas ganttChart, int currentTime, ScheduledProcess lastProcess, double startX, int lastStartTime) {
        // Draw last process
        if (lastProcess != null) {
            GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), lastProcess.getName(), startX, lastStartTime);
            startX += GanttChartDrawer.UNIT_WIDTH;
        }

        // Display the final time after all processes are executed
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime),
                startX, GanttChartDrawer.POSITION_Y + 50);
    }

    static int executeProcessAndUpdateTime(Canvas ganttChart, int currentTime, ScheduledProcess process, double startX) {
        // Calculate process completion time, turnaround time, and waiting time
        int finishTime = currentTime + process.getBurstTime();
        int turnaroundTime = finishTime - process.getArrivalTime();
        int waitingTime = turnaroundTime - process.getBurstTime();

        // Draw the process block on the Gantt chart
        GanttChartDrawer.drawProcessBlock(ganttChart.getGraphicsContext2D(), process.getName(), startX, currentTime);

        // Update process details with calculated times
        process.setFinishTime(finishTime);
        process.setTurnaroundTime(turnaroundTime);
        process.setWaitingTime(waitingTime);
        process.setResponseTime(currentTime - process.getArrivalTime());

        // Return the updated current time (finish time of the process)
        return finishTime;
    }

    static int handleIdleAndUpdateTime(Canvas ganttChart, int currentTime, ScheduledProcess process, double startX) {
        // Draw idle block to indicate CPU is idle
        GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime);

        // return current time to the arrival time of the next process
        return process.getArrivalTime();
    }

}
