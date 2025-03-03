package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.models.RoundRobin;
import scheduling.utilities.Alert;
import scheduling.utilities.GanttChartDrawer;

import java.util.*;

public class RoundRobinController extends NormalProcess {

    @FXML
    private TextField timeQuantumInput;

    private int timeQuantum = 2;

    @FXML
    public void initialize() {
        setCellValue();
        timeQuantumInput.setText(String.valueOf(timeQuantum));

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        //startSimulation.setOnAction(event -> runRoundRobinScheduling());
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void simulate() {
        if (!validateInput()) return;

        takeTimeQuantumFromKeyboard();
        RoundRobin.simulate(processes, ganttChart, 0, timeQuantum);

        calculateAverageTime();
        displayProcessInfo();
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
