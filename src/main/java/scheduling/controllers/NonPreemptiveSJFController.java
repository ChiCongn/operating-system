package scheduling.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.utilities.Alert;
import scheduling.utilities.GanttChartDrawer;
import scheduling.utilities.InputHandler;

import java.util.*;

public class NonPreemptiveSJFController {
    @FXML
    private TableView<Process> nonPreemptivesjf;

    @FXML
    private TableColumn<Process, String> nonPreemptivesjfName;

    @FXML
    private TableColumn<Process, Integer> nonPreemptivesjfArrivalTime, nonPreemptivesjfBurstTime,
            nonPreemptivesjfFinishTime, nonPreemptivesjfTurnaroundTime, nonPreemptivesjfWaitingTime;

    @FXML
    private TextField processNames;

    @FXML
    private TextField arrivalTimes;

    @FXML
    private TextField burstTimes;

    @FXML
    private TextField filePath;

    @FXML
    private Button uploadFile, startSimulation, addProcess, refresh;

    @FXML
    private Canvas ganttChart;

    private final ObservableList<Process> processes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nonPreemptivesjfName.setCellValueFactory(new PropertyValueFactory<>("name"));
        nonPreemptivesjfArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        nonPreemptivesjfBurstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        nonPreemptivesjfFinishTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        nonPreemptivesjfTurnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        nonPreemptivesjfWaitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        nonPreemptivesjf.setItems(processes);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runNonPreemptiveSJFScheduling());
        addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void runNonPreemptiveSJFScheduling() {
        if (!validateInput()) return;

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        double startX = 20;
        int size = processes.size();

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


    private void handleFileUpload() {
        InputHandler.handleFileUpload(filePath, processes);
    }


    private boolean validateInput() {
        if (processes.isEmpty()) {
            InputHandler.parseManualInput(processNames, arrivalTimes, burstTimes, processes);
        }

        if (processes.isEmpty()) {
            handleFileUpload();
        }

        // Final check if no processes are found
        if (processes.isEmpty()) {
            Alert.showAlert("Error", "No processes found! Please upload a file or add processes manually.");
            return false;
        }

        return true;
    }

    private void refresh() {
        processes.clear();
        filePath.clear();
        processNames.clear();
        arrivalTimes.clear();
        burstTimes.clear();
        GanttChartDrawer.clearGanttChart(ganttChart);
    }

}
