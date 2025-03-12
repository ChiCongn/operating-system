package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.FCFS;
import scheduling.models.PreemptiveSJF;
import scheduling.models.Process;
import scheduling.models.RoundRobin;

public class MultilevelQueueController extends MultilevelQueue {

    int completedProcess = 0;

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
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    void simulate() {
        System.out.println("simulate multilevel queue");
        if (!validateInput()) return;
        allocateQueue();

        int[] currentTime = new int[1];
        //System.out.println("set current time: done!");
        RoundRobin.simulate(roundRobinQueue, ganttChart, currentTime, 2);
        completedProcess = PreemptiveSJF.simulate(preemptiveSJFQueue, preemptiveSJFGanttChart, completedProcess, currentTime);
        FCFS.simulate(fcfsQueue, fcfsGanttChart, currentTime);

        calculateAverageTime();
        displayProcessInfo();
    }

    void displayProcessInfo() {
        super.displayProcessInfo();

        if (!processes.isEmpty()) {
            StringBuilder queue = new StringBuilder();
            for (Process p : processes) {
                queue.append(p.getQueueLevel()).append(" ");
            }

            queues.setText(queue.toString());
        } else {
            queues.setText("No processes available");
        }
    }
}
