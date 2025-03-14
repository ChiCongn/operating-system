package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.PreemptiveSJF;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptiveSJFController extends NormalProcess {

    int completedProcess = 0;

    @FXML
    public void initialize() {
        setCellValue();

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        //startSimulation.setOnAction(event -> runPreemptiveSJFScheduling());
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void simulate() {
        if (!validateInput()) return;
        completedProcess = PreemptiveSJF.simulate(processes, ganttChart, completedProcess, 0);
        calculateAverageTime();
        displayProcessInfo();
    }

    void refresh() {
        super.refresh();
        completedProcess = 0;
    }
}
