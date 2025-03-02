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


public class fcfsController {

    @FXML
    private TableView<Process> fcfs;

    @FXML
    private TableColumn<Process, String> fcfsName;

    @FXML
    private TableColumn<Process, Integer> fcfsArrivalTime, fcfsBurstTime, fcfsFinishTime, fcfsTurnaroundTime, fcfsWaitingTime;

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
        fcfsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fcfsArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        fcfsBurstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        fcfsFinishTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        fcfsTurnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        fcfsWaitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        fcfs.setItems(processes);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runfcfsScheduling());
        addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
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

    private void runfcfsScheduling() {
        if (!validateInput()) return;

        int currentTime = 0;
        double startX = 20;

        for (Process process : processes) {
            int startTime = Math.max(currentTime, process.getArrivalTime());
            int finishTime = startTime + process.getBurstTime();
            int turnaroundTime = finishTime - process.getArrivalTime();
            int waitingTime = turnaroundTime - process.getBurstTime();

            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(), process.getName(),
                    startX, startTime, process.getBurstTime());

            process.setCompletionTime(finishTime);
            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);

            currentTime = finishTime;
            startX += process.getBurstTime() * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);

        fcfs.refresh();
    }
}
