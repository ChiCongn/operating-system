package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.utilities.Alert;
import scheduling.utilities.GanttChartDrawer;
import scheduling.utilities.InputHandler;

import java.util.*;

public class RoundRobinController extends CommonController {

    @FXML
    private TextField timeQuantumInput;

    private int timeQuantum = 2;

    @FXML
    public void initialize() {
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        completionTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        processTableView.setItems(processes);

        timeQuantumInput.setText(String.valueOf(timeQuantum));

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runRoundRobinScheduling());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    public void runRoundRobinScheduling() {
        if (!validateInput()) return;
        takeTimeQuantumFromKeyboard();

        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;
        int totalProcesses = processes.size();
        double startX = 20; // X-coordinate for drawing Gantt chart

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        int index = 0;

        readyQueue.add(processes.get(index++));

        while (!readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();

            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());

            // Deduct executed time from the remaining time
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
            currentTime += executionTime;

            // Draw the execution block in the Gantt chart
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.getName(), startX, currentTime - executionTime, executionTime);
            startX += executionTime * GanttChartDrawer.UNIT_WIDTH; // Update X-position

            // If the process is fully completed, set its completion, turnaround, and waiting times
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }

            // Add new processes that have arrived by the current time
            while (index < totalProcesses && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // If the current process is not yet complete, add it back to the queue for the next cycle
            if (currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            }
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);
    }

    public void takeTimeQuantumFromKeyboard() {
        String input = timeQuantumInput.getText().trim();

        try {
            int temp = Integer.parseInt(input);
            if (temp > 0) {
                timeQuantum = temp;
                timeQuantumInput.setText(String.valueOf(timeQuantum));
            } else {
                System.out.println("Time quantum must be a positive integer.");
                Alert.showAlert("Invalid Input", "Time quantum must be a positive integer.");
            }
        } catch (NumberFormatException e) {
            Alert.showAlert("Invalid Input", "Please enter a valid integer.");
        }
    }
}
