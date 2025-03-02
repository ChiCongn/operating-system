package scheduling.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;


public class FCFSController extends NormalProcess {
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
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void runfcfsScheduling() {
        if (validateInput()) return;

        System.out.println("run fcfs simulation");
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
