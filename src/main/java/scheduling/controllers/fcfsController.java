package scheduling.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private Button uploadFile, startSimulation, addProcess;

    @FXML
    private Canvas ganttChart;

    private final ObservableList<Process> processes = FXCollections.observableArrayList();
    Set<String> uniqueProcessNames = new HashSet<>();

    @FXML
    public void initialize() {
        fcfsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fcfsArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        fcfsBurstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        fcfsFinishTime.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        fcfsTurnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        fcfsWaitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        fcfs.setItems(processes);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runfcfsScheduling());
        addProcess.setOnAction(event -> addManualProcess());
    }

    private void parseManualInput() {
        try {
            // Split inputs into lists
            List<String> names = Arrays.asList(processNames.getText().trim().split("\\s+"));
            List<Integer> arrivals = Arrays.stream(arrivalTimes.getText().trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<Integer> bursts = Arrays.stream(burstTimes.getText().trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // Validate input lengths
            if (names.size() != arrivals.size() || names.size() != bursts.size()) {
                System.out.println("Mismatch in input sizes. Ensure each process has arrival and burst times.");
                return;
            }

            for (int i = 0; i < names.size(); i++) {
                processes.add(new Process(names.get(i), arrivals.get(i), bursts.get(i)));
            }

            // Clear input fields
            processNames.clear();
            arrivalTimes.clear();
            burstTimes.clear();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers for Arrival and Burst Time.");
        }
    }

    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePath.setText(selectedFile.getAbsolutePath());
            loadProcessesFromFile(selectedFile);
        }
    }

    private void loadProcessesFromFile(File file) {
        processes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("[,\\s]+");
                if (parts.length == 3) {  // Expected Format: ProcessName, ArrivalTime, BurstTime
                    String processName = parts[0].trim();
                    int arrivalTime = Integer.parseInt(parts[1].trim());
                    int burstTime = Integer.parseInt(parts[2].trim());

                    if (uniqueProcessNames.contains(processName)) {
                        System.out.println("Warning: Duplicate process name '" + processName + "' found. Skipping...");
                        continue; // Skip this process
                    }

                    processes.add(new Process(processName, arrivalTime, burstTime));
                }
            }
        } catch (IOException | NumberFormatException e) {
            showAlert("Error", "Invalid file format. Expected: Process, ArrivalTime, BurstTime");
        }
    }

    private boolean validateInput() {
        parseManualInput();

        if (processes.isEmpty()) {
            showAlert("Error", "No processes found! Please upload a file or add processes manually.");
            return false;
        }
        return true;
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

            process.setFinishTime(finishTime);
            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);

            currentTime = finishTime;
            startX += process.getBurstTime() * GanttChartDrawer.UNIT_WIDTH;
        }

        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX,
                GanttChartDrawer.POSITION_Y + 50);

        fcfs.refresh();
    }

    private void addManualProcess() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Process");
        dialog.setHeaderText("Enter Process Details (Format: ProcessName,ArrivalTime,BurstTime)");
        dialog.setContentText("Input:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 3) {
                try {
                    String processName = parts[0].trim();
                    int arrivalTime = Integer.parseInt(parts[1].trim());
                    int burstTime = Integer.parseInt(parts[2].trim());
                    processes.add(new Process(processName, arrivalTime, burstTime));
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
                }
            } else {
                showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
            }
        });
    }


    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
