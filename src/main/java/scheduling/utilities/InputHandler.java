package scheduling.utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scheduling.models.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class InputHandler {
    public static void parseManualInput(TextField processNames, TextField arrivalTimes,
                                        TextField burstTimes, ObservableList<Process> processes) {
        processes.clear();
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

            /*// Clear input fields
            processNames.clear();
            arrivalTimes.clear();
            burstTimes.clear();*/

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers for Arrival and Burst Time.");
        }
    }

    public static void handleFileUpload(TextField filePath, ObservableList<Process> processes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePath.setText(selectedFile.getAbsolutePath());
            loadProcessesFromFile(selectedFile, processes);
        }
    }

    public static void addManualProcess(ObservableList<Process> processes) {
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
                    Alert.showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
                }
            } else {
                Alert.showAlert("Error", "Invalid input format. Use: ProcessName,ArrivalTime,BurstTime");
            }
        });
    }

    private static void loadProcessesFromFile(File file, ObservableList<Process> processes) {
        processes.clear();
        Set<String> uniqueProcessNames = new HashSet<>();
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

                    uniqueProcessNames.add(processName);
                    processes.add(new Process(processName, arrivalTime, burstTime));
                }
            }
        } catch (IOException | NumberFormatException e) {
            //showAlert("Error", "Invalid file format. Expected: Process, ArrivalTime, BurstTime");
            System.out.println("Error: Invalid file format. Expected: Process, ArrivalTime, BurstTime");
        }
    }
}
