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
import scheduling.utilities.SceneManager;

import java.util.List;

public abstract class CommonController {
    @FXML
    TableView<Process> processTableView;

    @FXML
    TableColumn<Process, String> processName;

    @FXML
    TableColumn<Process, Integer> arrivalTime, burstTime,
            completionTime, turnaroundTime, waitingTime, responseTime;

    @FXML
    TextField processNames;

    @FXML
    TextField arrivalTimes;

    @FXML
    TextField burstTimes;

    @FXML
    TextField filePath;

    @FXML
    Button uploadFile, startSimulation, refresh;

    @FXML
    Label averageResponseTime, averageTurnaroundTime, averageWaitingTime;

    @FXML
    Canvas ganttChart;

    final ObservableList<Process> processes = FXCollections.observableArrayList();

    abstract void handleFileUpload();

    abstract  boolean validateInput();

    void refresh() {
        processes.clear();
        filePath.clear();
        processNames.clear();
        arrivalTimes.clear();
        burstTimes.clear();
        GanttChartDrawer.clearGanttChart(ganttChart);
    }

    void calculateAverageTime() {
        double responseTimes = 0.0, turnaroundTimes = 0.0, waitingTimes = 0.0;
        int n = processes.size();
        for (Process p : processes) {
            responseTimes += p.getResponseTime();
            turnaroundTimes += p.getTurnaroundTime();
            waitingTimes += p.getWaitingTime();
        }

        averageResponseTime.setText(String.format("%.2f", responseTimes / n));
        averageTurnaroundTime.setText(String.format("%.2f", turnaroundTimes / n));
        averageWaitingTime.setText(String.format("%.2f", waitingTimes / n));
    }


    void setCellValue() {
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        completionTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        responseTime.setCellValueFactory(new PropertyValueFactory<>("responseTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        processTableView.setItems(processes);
    }

    void displayProcessInfo() {
        if (!processes.isEmpty()) {
            System.out.println("display process info");

            StringBuilder processesName = new StringBuilder();
            StringBuilder arrivalTime = new StringBuilder();
            StringBuilder burstTime = new StringBuilder();
            for (Process p : processes) {
                processesName.append(p.getName()).append(" ");
                arrivalTime.append(p.getArrivalTime()).append(" ");
                burstTime.append(p.getBurstTime()).append(" ");
            }

            processNames.setText(processesName.toString());
            arrivalTimes.setText(arrivalTime.toString());
            burstTimes.setText(burstTime.toString());
        } else {
            processNames.setText("No processes available");
            arrivalTimes.setText("No processes available");
            burstTimes.setText("No processes available");
        }

    }

    public void switchToFCFS() {
        FCFSController fcfsController = SceneManager.switchScene(SceneManager.FCFS_PATH);
    }

    public void switchToPreemptiveSJF() {
        PreemptiveSJFController preemptiveSJF_Controller = SceneManager.switchScene(SceneManager.PREEMPTIVE_SJF);
    }

    public void switchToNonPreemptiveSJF() {
        NonPreemptiveSJFController nonPreemptiveSJF_Controller = SceneManager.switchScene(SceneManager.NON_PREEMPTIVE_SJF);
    }

    public void switchToRoundRobin() {
        RoundRobinController roundRobin_Controller = SceneManager.switchScene(SceneManager.ROUND_ROBIN);
    }

    public void switchToPreemptivePriority() {
        PreemptivePriorityController preemptivePriorityController = SceneManager.switchScene(SceneManager.PREEMPTIVE_PRIORITY);
    }

    public void switchToNonPreemptivePriority() {
        NonPreemptivePriorityController nonPreemptivePriorityController = SceneManager.switchScene(SceneManager.NON_PREEMPTIVE_PRIORITY);
    }

    public void switchToMultilevelQueue() {
        MultilevelQueueController multilevelQueueController = SceneManager.switchScene(SceneManager.MULTILEVEL_QUEUE);
    }

    public void switchToMultilevelFeedbackQueue() {
        //MultilevelFeedbackQueueController multilevelFeedbackQueue_Controller = SceneManager.switchScene(SceneManager.MULTILEVEL_FEEDBACK_QUEUE);
    }

}
