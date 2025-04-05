package edu.tool.controllers;

import edu.tool.algorithms.scheduling.Priority;
import edu.tool.algorithms.scheduling.RoundRobin;
import edu.tool.algorithms.scheduling.SJF;
import edu.tool.enums.SchedulingAlgorithm;
import edu.tool.algorithms.scheduling.FCFS;

import edu.tool.models.ScheduledProcess;
import edu.tool.utils.Alert;
import edu.tool.utils.Drawer;
import edu.tool.utils.InputHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;


public class SchedulingController {
    @FXML
    TableView<ScheduledProcess> infoTable;
    @FXML
    TableColumn<ScheduledProcess, String> processName;
    @FXML
    TableColumn<ScheduledProcess, Integer> arrivalTime;
    @FXML
    TableColumn<ScheduledProcess, Integer> burstTime;
    @FXML
    TableColumn<ScheduledProcess, Integer> finishTime;
    @FXML
    TableColumn<ScheduledProcess, Integer> responseTime;
    @FXML
    TableColumn<ScheduledProcess, Integer> turnaroundTime;
    @FXML
    TableColumn<ScheduledProcess, Integer> waitingTime;

    @FXML
    TextField processNameField;
    @FXML
    TextField arrivalTimeField;
    @FXML
    TextField burstTimeField;
    @FXML
    TextField priorityField;
    @FXML
    TextField timeQuantumField;

    @FXML
    ChoiceBox<SchedulingAlgorithm> schedulingAlgorithmChoiceBox;
    @FXML
    Label schedulingAlgorithm;

    @FXML
    Button run;

    @FXML
    HBox isRoundRobin;
    @FXML
    Canvas ganttChart;

    @FXML
    Label averageResponseTime;
    @FXML
    Label averageTurnaroundTime;
    @FXML
    Label averageWaitingTime;

    final ObservableList<ScheduledProcess> processes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        schedulingAlgorithmChoiceBox.getItems().addAll(SchedulingAlgorithm.values());
        schedulingAlgorithmChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateAlgorithmLabel(newValue);
            // Set visibility based on the selected algorithm
            isRoundRobin.setVisible(newValue == SchedulingAlgorithm.ROUND_ROBIN);
        });
        schedulingAlgorithmChoiceBox.setValue(SchedulingAlgorithm.FCFS);
        setCellValue();
        run.setOnAction(event -> run());
    }

    void setCellValue() {
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        finishTime.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        responseTime.setCellValueFactory(new PropertyValueFactory<>("responseTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        infoTable.setItems(processes);
    }

    @FXML
    void run() {
        if (!isAlgorithmSelected()) {
            Alert.showNotification("Selection Error", "Please choose a scheduling algorithm before proceeding!");
            return;
        }

        if (!getInputValues()) {
            return; // Stop execution if input validation fails
        }
        Drawer.clearGanttChart(ganttChart);
        runSelectedAlgorithm(schedulingAlgorithmChoiceBox.getValue());
        updateAverageTimes();
    }

    void updateAlgorithmLabel(SchedulingAlgorithm selectedAlgorithm) {
        switch (selectedAlgorithm) {
            case FCFS:
                schedulingAlgorithm.setText("First Come First Serve");
                break;
            case PREEMPTIVE_SJF:
                schedulingAlgorithm.setText("Preemptive Shortest Job First");
                break;
            case NON_PREEMPTIVE_SJF:
                schedulingAlgorithm.setText("Non-Preemptive Shortest Job First");
                break;
            case ROUND_ROBIN:
                schedulingAlgorithm.setText("Round Robin");
                break;
            case PREEMPTIVE_PRIORITY:
                schedulingAlgorithm.setText("Preemptive Priority");
                break;
            case NON_PREEMPTIVE_PRIORITY:
                schedulingAlgorithm.setText("Non-Preemptive Priority");
                break;
            case MULTI_LEVEL_QUEUE:
                schedulingAlgorithm.setText("Multi-Level Queue");
                break;
            case MULTI_LEVEL_FEEDBACK_QUEUE:
                schedulingAlgorithm.setText("Multi-Level Feedback Queue");
                break;
            default:
                schedulingAlgorithm.setText("Unknown Algorithm");
        }
    }


    private boolean isAlgorithmSelected() {
        SchedulingAlgorithm selectedAlgorithm = schedulingAlgorithmChoiceBox.getValue();
        return selectedAlgorithm != null;
    }

    private void runSelectedAlgorithm(SchedulingAlgorithm selectedAlgorithm) {
        switch (selectedAlgorithm) {
            case FCFS:
                runFCFS();
                break;
            case PREEMPTIVE_SJF:
                runSJFPreemptive();
                break;
            case NON_PREEMPTIVE_SJF:
                runSJFNonPreemptive();
                break;
            case ROUND_ROBIN:
                runRoundRobin();
                break;
            case PREEMPTIVE_PRIORITY:
                runPriorityPreemptive();
                break;
            case NON_PREEMPTIVE_PRIORITY:
                runPriorityNonPreemptive();
                break;
            case MULTI_LEVEL_QUEUE:
                runMultiLevelQueue();
                break;
            case MULTI_LEVEL_FEEDBACK_QUEUE:
                runMultiLevelFeedbackQueue();
                break;
            default:
                System.out.println("Invalid algorithm selection!");
        }
    }

    private void runFCFS() {
        System.out.println("Running First Come First Serve (FCFS)");
        FCFS.simulate(new ArrayList<>(processes), ganttChart, 0);
    }

    private void runSJFPreemptive() {
        System.out.println("Running Shortest Job First (Preemptive)");
        SJF.simulatePreemptive(new ArrayList<>(processes), ganttChart, 0);
    }

    private void runSJFNonPreemptive() {
        System.out.println("Running Shortest Job First (Non-Preemptive)");
        SJF.simulateNonPreemptive(new ArrayList<>(processes), ganttChart, 0);
    }

    private void runRoundRobin() {
        System.out.println("Running Round Robin");
        RoundRobin.simulate(new ArrayList<>(processes), ganttChart, getTimeQuantum(), 0);
    }

    private void runPriorityPreemptive() {
        System.out.println("Running Priority Scheduling (Preemptive)");
        Priority.simulatePreemptive(new ArrayList<>(processes), ganttChart, 0);
    }

    private void runPriorityNonPreemptive() {
        System.out.println("Running Priority Scheduling (Non-Preemptive)");
        Priority.simulateNonPreemptive(new ArrayList<>(processes), ganttChart, 0);
    }

    private void runMultiLevelQueue() {
        System.out.println("Running Multi-Level Queue Scheduling");
        Alert.showNotification("Feature Under Development",
                "This feature is currently under development. Stay tuned!");
        //MultiLevelQueue.simulate(processes, ganttChart);
    }

    private void runMultiLevelFeedbackQueue() {
        System.out.println("Running Multi-Level Feedback Queue");
        Alert.showNotification("Feature Under Development",
                "This feature is currently under development. Stay tuned!");
        //MultiLevelFeedbackQueue.simulate(processes, ganttChart);
    }

    private int getTimeQuantum() {
        return InputHandler.convertStringToInteger(timeQuantumField.getText());
    }

    private void updateAverageTimes() {
        if (processes.isEmpty()) {
            averageResponseTime.setText("N/A");
            averageTurnaroundTime.setText("N/A");
            averageWaitingTime.setText("N/A");
            return;
        }

        double totalResponseTime = 0.0, totalTurnaroundTime = 0.0, totalWaitingTime = 0.0;
        for (ScheduledProcess p : processes) {
            totalResponseTime += p.getResponseTime();
            totalTurnaroundTime += p.getTurnaroundTime();
            totalWaitingTime += p.getWaitingTime();
        }

        int processCount = processes.size();
        averageResponseTime.setText(String.format("%.4f", totalResponseTime / processCount));
        averageTurnaroundTime.setText(String.format("%.4f", totalTurnaroundTime / processCount));
        averageWaitingTime.setText(String.format("%.4f", totalWaitingTime / processCount));
    }

    private boolean getInputValues() {
        try {
            // retrieve input values
            String[] processNames = InputHandler.convertStringToStringArray(processNameField.getText());
            int[] arrivalTimes = InputHandler.convertStringToIntegerArray(arrivalTimeField.getText());
            int[] burstTimes = InputHandler.convertStringToIntegerArray(burstTimeField.getText());
            int[] priorities = InputHandler.convertStringToIntegerArray(priorityField.getText());
            int timeQuantum = isRoundRobin.isVisible() ? InputHandler.convertStringToInteger(timeQuantumField.getText()) : -1;

            if (!validateInput(processNames, arrivalTimes, burstTimes, priorities, timeQuantum)) {
                return false;
            }

            System.out.println("Input values successfully retrieved and validated.");

            ObservableList<ScheduledProcess> processList = FXCollections.observableArrayList();
            for (int i = 0; i < processNames.length; i++) {
                int priority = (i < priorities.length) ? priorities[i] : -1; // Use -1 if priority is not provided
                processList.add(new ScheduledProcess(processNames[i], arrivalTimes[i], burstTimes[i], priority));
            }

            processes.setAll(processList);
            if (processes.isEmpty()) {
                Alert.showNotification("Input Error", "No processes found to run.");
                return false;
            }
            return true;

        } catch (Exception e) {
            Alert.showNotification("Input Error", "An error occurred while processing input: " + e.getMessage());
        }
        return false;
    }

    /**
     * Validates the scheduling input values.
     * @return true if valid, false otherwise
     */
    private boolean validateInput(String[] processNames, int[] arrivalTimes, int[] burstTimes, int[] priorities, int timeQuantum) {
        if (!checkInputLengths(processNames, arrivalTimes, burstTimes, priorities)) {
            return false;
        }
        if (!validateProcessTimes(arrivalTimes, burstTimes)) {
            return false;
        }
        if (!validatePriority(priorities)) {
            return false;
        }
        if (isRoundRobin.isVisible() && timeQuantum <= 0) {
            Alert.showNotification("Invalid Time Quantum", "Time quantum must be a positive integer.");
            return false;
        }
        return true;
    }

    /**
     * Checks if all input arrays have the same length.
     */
    private boolean checkInputLengths(String[] processNames, int[] arrivalTimes, int[] burstTimes, int[] priorities) {
        if (processNames.length != arrivalTimes.length || arrivalTimes.length != burstTimes.length) {
            Alert.showNotification("Input Mismatch", "Ensure all input fields have the same number of values.");
            return false;
        }

        if (requiresPriority(schedulingAlgorithmChoiceBox.getValue()) && priorities.length != arrivalTimes.length) {
            Alert.showNotification("Input Mismatch", "Priority values must match the number of processes.");
            return false;
        }

        return true;
    }

    /**
     * Validates that arrival times are non-negative and burst times are positive.
     */
    private boolean validateProcessTimes(int[] arrivalTimes, int[] burstTimes) {
        for (int i = 0; i < burstTimes.length; i++) {
            if (arrivalTimes[i] < 0 || burstTimes[i] <= 0) {
                Alert.showNotification("Invalid Input", "Arrival times must be non-negative, and burst times must be positive.");
                return false;
            }
        }
        return true;
    }

    private boolean validatePriority(int[] priorities) {
        if (requiresPriority(schedulingAlgorithmChoiceBox.getValue())) {
            if (priorities == null || priorities.length == 0) {
                Alert.showNotification("Invalid Input", "This scheduling algorithm requires priorities.");
                return false;
            }
            for (int priority : priorities) {
                if (priority < 0) {
                    Alert.showNotification("Invalid Input", "Priority values must be non-negative.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean requiresPriority(SchedulingAlgorithm algorithm) {
        return algorithm == SchedulingAlgorithm.PREEMPTIVE_PRIORITY ||
                algorithm == SchedulingAlgorithm.NON_PREEMPTIVE_PRIORITY;
    }
}
