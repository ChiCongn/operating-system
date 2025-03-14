package scheduling.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import scheduling.models.Process;
import scheduling.utilities.Alert;
import scheduling.utilities.GanttChartDrawer;
import scheduling.utilities.InputHandler;

public class MultilevelQueue extends CommonController {

    /* inherits from CommonController. it's a bad name but ..., so now treats it as Round Robin gantt chart
    Canvas ganttChart;*/
    @FXML
    Canvas preemptiveSJFGanttChart;
    @FXML
    Canvas fcfsGanttChart;

    @FXML
    TextField queues;

    int completedProcessInPreemptiveSJF = 0;

    final ObservableList<Process> roundRobinQueue = FXCollections.observableArrayList();
    final ObservableList<Process> preemptiveSJFQueue = FXCollections.observableArrayList();
    final ObservableList<Process> fcfsQueue = FXCollections.observableArrayList();

    @Override
    void handleFileUpload() {
        InputHandler.handleFileUploadMultilevelQueue(filePath, processes);
    }

    @Override
    boolean validateInput() {
        if (processes.isEmpty()) {
            InputHandler.parseManualMultilevelQueueInput(processNames, arrivalTimes, burstTimes, queues, processes);
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
        super.refresh();
        queues.clear();
        completedProcessInPreemptiveSJF = 0;

        roundRobinQueue.clear();
        preemptiveSJFQueue.clear();
        fcfsQueue.clear();

        GanttChartDrawer.clearGanttChart(preemptiveSJFGanttChart);
        GanttChartDrawer.clearGanttChart(fcfsGanttChart);
    }

    void allocateQueue() {
        System.out.println("allocate processes for queue");
        for (Process p : processes) {
            if (p.getQueueLevel() == 1) {
                roundRobinQueue.add(p);
            } else if (p.getQueueLevel() == 2) {
                preemptiveSJFQueue.add(p);
            } else {
                fcfsQueue.add(p);
            }
        }
    }
}
