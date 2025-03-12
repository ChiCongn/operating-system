package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;

public class FCFS {
    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int[] currentTime) {
        System.out.println("Simulating FCFS Scheduling");

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        double startX = 20;

        for (Process process : processes) {
            // If there's a gap (CPU idle time), draw an idle block
            if (currentTime[0] < process.arrivalTime) {
                int idleStart = currentTime[0];
                int idleDuration = process.arrivalTime - currentTime[0];
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, idleStart, idleDuration);

                startX += idleDuration * GanttChartDrawer.UNIT_WIDTH;
                currentTime[0] = process.arrivalTime;
            }

            int startTime = currentTime[0];
            int finishTime = startTime + process.burstTime;
            int turnaroundTime = finishTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), process.name, startX, startTime, process.burstTime);

            // Update process
            process.completionTime = finishTime;
            process.turnaroundTime = turnaroundTime;
            process.waitingTime = waitingTime;
            process.responseTime = startTime - process.arrivalTime;

            // Update time tracking
            currentTime[0] = finishTime;
            startX += process.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);
    }

    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime) {
        System.out.println("Simulating FCFS Scheduling");

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        double startX = 20;

        for (Process process : processes) {
            // If there's a gap (CPU idle time), draw an idle block
            if (currentTime < process.arrivalTime) {
                int idleStart = currentTime;
                int idleDuration = process.arrivalTime - currentTime;
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, idleStart, idleDuration);

                startX += idleDuration * GanttChartDrawer.UNIT_WIDTH;
                currentTime = process.arrivalTime;
            }

            int startTime = currentTime;
            int finishTime = startTime + process.burstTime;
            int turnaroundTime = finishTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), process.name, startX, startTime, process.burstTime);

            // Update process
            process.completionTime = finishTime;
            process.turnaroundTime = turnaroundTime;
            process.waitingTime = waitingTime;
            process.responseTime = startTime - process.arrivalTime;

            // Update time tracking
            currentTime = finishTime;
            startX += process.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
    }

}
