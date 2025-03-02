package scheduling.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import scheduling.models.Process;
import scheduling.utilities.Alert;
import scheduling.utilities.GanttChartDrawer;
import scheduling.utilities.InputHandler;
import scheduling.utilities.SceneManager;

public class CommonController {
    @FXML
    TableView<Process> processTableView;

    @FXML
    TableColumn<Process, String> processName;

    @FXML
    TableColumn<Process, Integer> arrivalTime, burstTime,
            completionTime, turnaroundTime, waitingTime;

    @FXML
    TextField processNames;

    @FXML
    TextField arrivalTimes;

    @FXML
    TextField burstTimes;

    @FXML
    TextField filePath;

    @FXML
    Button uploadFile, startSimulation, addProcess, refresh;

    @FXML
    Canvas ganttChart;

    final ObservableList<Process> processes = FXCollections.observableArrayList();
    void handleFileUpload() {
        InputHandler.handleFileUpload(filePath, processes);
    }

    boolean validateInput() {
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

    void refresh() {
        processes.clear();
        filePath.clear();
        processNames.clear();
        arrivalTimes.clear();
        burstTimes.clear();
        GanttChartDrawer.clearGanttChart(ganttChart);
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
        //PreemptivePriorityController preemptivePriorityController = SceneManager.switchScene(SceneManager.PREEMPTIVE_PRIORITY);
    }

    public void switchToNonPreemptivePriority() {
        //NonPreemptivePriorityController nonPreemptivePriorityController = SceneManager.switchScene(SceneManager.NON_PREEMPTIVE_PRIORITY);
    }

    public void switchToMultilevelQueue() {
        //MultilevelQueueController multilevelQueueController = SceneManager.switchScene(SceneManager.MULTILEVEL_QUEUE);
    }

    public void switchToMultilevelFeedbackQueue() {
        //MultilevelFeedbackQueueController multilevelFeedbackQueue_Controller = SceneManager.switchScene(SceneManager.MULTILEVEL_FEEDBACK_QUEUE);
    }

}
