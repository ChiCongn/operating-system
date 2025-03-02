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

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptiveSJFController {
    @FXML
    private TableView<Process> preemptivesjf;

    @FXML
    private TableColumn<Process, String> processName;

    @FXML
    private TableColumn<Process, Integer> arrivalTime, burstTime,
            completionTime, turnaroundTime, waitingTime;

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
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        completionTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        preemptivesjf.setItems(processes);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runPreemptiveSJFScheduling());
        addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }
    private void runPreemptiveSJFScheduling() {
        if (!validateInput()) return;

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0, completed = 0;
        double startX = 20;
        int[] remainingTime = new int[processes.size()];

        for (int i = 0; i < processes.size(); i++) {
            remainingTime[i] = processes.get(i).getBurstTime();
        }

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
        );

        Process currentProcess = null;
        Process lastProcess = null; // to track Gantt chart segments
        int lastProcessStartTime = 0;

        while (completed < processes.size()) {
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
                    completed++;
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
