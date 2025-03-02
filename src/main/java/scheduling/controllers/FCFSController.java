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


public class FCFSController extends CommonController {
    @FXML
    public void initialize() {
        processName.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        completionTime.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        turnaroundTime.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTime.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        processTableView.setItems(processes);


        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> runfcfsScheduling());
        addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
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

        processTableView.refresh();
    }
}
