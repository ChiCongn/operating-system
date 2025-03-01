package scheduling.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scheduling.models.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class fcfsController {

    @FXML
    private TableView<Process> fcfs;

    @FXML
    private TableColumn<Process, String> fcfsName;

    @FXML
    private TableColumn<Process, Integer> fcfsArrivalTime, fcfsBurstTime, fcfsFinishTime, fcfsTurnaroundTime, fcfsWaitingTime;

    @FXML
    private TextField filePath;

    @FXML
    private Button uploadFile, startSimulation, addProcess;

    private final ObservableList<Process> processList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fcfsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fcfsArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        fcfsBurstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        fcfsFinishTime.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        fcfsTurnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        fcfsWaitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));


        fcfs.setItems(processList);

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runfcfsScheduling());
        addProcess.setOnAction(event -> addManualProcess());
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
        processList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {  // Expected Format: ProcessName, ArrivalTime, BurstTime
                    String processName = parts[0].trim();
                    int arrivalTime = Integer.parseInt(parts[1].trim());
                    int burstTime = Integer.parseInt(parts[2].trim());
                    processList.add(new Process(processName, arrivalTime, burstTime));
                }
            }
        } catch (IOException | NumberFormatException e) {
            showAlert("Error", "Invalid file format. Expected: Process, ArrivalTime, BurstTime");
        }
    }

    private void runfcfsScheduling() {
        if (processList.isEmpty()) {
            showAlert("Error", "No processes found! Please upload a file or add processes manually.");
            return;
        }

        int currentTime = 0;
        for (Process process : processList) {
            int startTime = Math.max(currentTime, process.getArrivalTime());
            int finishTime = startTime + process.getBurstTime();
            int turnaroundTime = finishTime - process.getArrivalTime();
            int waitingTime = turnaroundTime - process.getBurstTime();

            process.setFinishTime(finishTime);
            process.setTurnaroundTime(turnaroundTime);
            process.setWaitingTime(waitingTime);

            currentTime = finishTime;
        }

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
                    processList.add(new Process(processName, arrivalTime, burstTime));
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
                }
            } else {
                showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
