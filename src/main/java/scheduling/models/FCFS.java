package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

public class FCFS {
    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int[] currentTime) {
        System.out.println("simulate fcfs");
        double startX = 20;

        for (Process process : processes) {
            int startTime = Math.max(currentTime[0], process.arrivalTime);
            int finishTime = startTime + process.burstTime;
            int turnaroundTime = finishTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), process.name, startX, startTime, process.burstTime);

            process.completionTime = finishTime;
            process.turnaroundTime = turnaroundTime;
            process.waitingTime = waitingTime;

            currentTime[0] = finishTime;
            startX += process.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);
    }

    public static void simulate(ObservableList<Process> processes, Canvas ganttChart, int currentTime) {
        System.out.println("simulate fcfs");
        double startX = 20;

        for (Process process : processes) {
            int startTime = Math.max(currentTime, process.arrivalTime);
            int finishTime = startTime + process.burstTime;
            int turnaroundTime = finishTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), process.name, startX, startTime, process.burstTime);

            process.completionTime = finishTime;
            process.turnaroundTime = turnaroundTime;
            process.waitingTime = waitingTime;

            currentTime = finishTime;
            startX += process.burstTime * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
    }

}
