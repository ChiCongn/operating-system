package edu.tool.algorithms.scheduling;

import edu.tool.models.ScheduledProcess;
import edu.tool.utils.GanttChartDrawer;
import javafx.scene.canvas.Canvas;
import java.util.Comparator;
import java.util.List;

public class FCFS {
    public static int simulateAndUpdateTime(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating FCFS Scheduling");
        double startX = GanttChartDrawer.START_X;

        // Sort processes by arrival time, if two processes have the same arrival time, sort by priority
        processes.sort(Comparator
                .comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getPriority)
        );
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

    public static void simulate(List<ScheduledProcess> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating FCFS Scheduling");
        double startX = GanttChartDrawer.START_X;

        // Sort processes by arrival time, and if two processes have the same arrival time, sort by priority
        processes.sort(Comparator
                .comparingInt(ScheduledProcess::getArrivalTime)
                .thenComparingInt(ScheduledProcess::getPriority)
        );

        // Ensure current time is not before the first process' arrival time
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
