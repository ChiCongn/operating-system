package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptivePriorityController extends CommonController {
    @FXML
    private TextField priorities;

    int completedProcess = 0;

    @FXML
    public void initialize() {
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        completionTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        processTableView.setItems(processes);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUploadPriorityProcess());
        startSimulation.setOnAction(event -> runPreemptivePriorityScheduling());
        //addProcess.setOnAction(event -> InputHandler.addManualPriorityProcess(processes));
        refresh.setOnAction(event -> refreshWithPriority());
    }

    private void runPreemptivePriorityScheduling() {
        if (!validatePriorityInput(priorities)) return;
        System.out.println("run preemptive priority");

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        double startX = 20;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getPriority)
        );

        Process currentProcess = null;
        Process lastProcess = null; // to track Gantt chart segments
        int lastProcessStartTime = 0;

        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the queue
            for (Process p : processes) {
                if (p.getArrivalTime() == currentTime) {
                    readyQueue.add(p);
                }
            }

            // If a process is running, add it back to the queue if it's not finished
            if (currentProcess != null && currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            }

            // Pick the process with the shortest remaining burst time
            if (!readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();

                // If the process changes, draw the previous one in Gantt Chart
                if (lastProcess != null && lastProcess != currentProcess) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.getName(), startX, lastProcessStartTime, currentTime - lastProcessStartTime);
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                    lastProcessStartTime = currentTime;
                }
                lastProcess = currentProcess;

                // execute process for 1 time unit
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                if (currentProcess.getRemainingTime() == 0) { // process finishes
                    completedProcess++;
                    currentProcess.setCompletionTime(currentTime + 1);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                }
            }

            currentTime++;
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    lastProcess.getName(), startX, lastProcessStartTime, currentTime - lastProcessStartTime);
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }

    private void refreshWithPriority() {
        refresh();
        priorities.clear();
        completedProcess = 0;
    }
}
