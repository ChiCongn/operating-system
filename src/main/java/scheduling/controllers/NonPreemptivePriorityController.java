package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class NonPreemptivePriorityController extends CommonController {
    @FXML
    private TextField priorities;


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
        startSimulation.setOnAction(event -> runNonPreemptivePriorityScheduling());
        //addProcess.setOnAction(event -> InputHandler.addManualPriorityProcess(processes));
        refresh.setOnAction(event -> refreshWithPriority());
    }

    private void runNonPreemptivePriorityScheduling() {
        if (!validatePriorityInput(priorities)) return;
        System.out.println("run non preemptive priority");

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        double startX = 20;
        int size = processes.size();

        int index = 0; // Track position in processes list
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));

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

            // Execute the process
            currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());
            currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), currentProcess.getName(),
                    startX, currentTime, currentProcess.getBurstTime());

            currentTime += currentProcess.getBurstTime();
            startX += currentProcess.getBurstTime() * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }

    private void refreshWithPriority() {
        refresh();
        priorities.clear();
    }
}
