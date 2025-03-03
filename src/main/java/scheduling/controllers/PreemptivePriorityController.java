package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.PreemptivePriority;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptivePriorityController extends Priority {

    int completedProcess = 0;

    @FXML
    public void initialize() {
        setCellValue();

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        //startSimulation.setOnAction(event -> runPreemptivePriorityScheduling());
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualPriorityProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void simulate() {
        if (!validateInput()) return;
        PreemptivePriority.simulate(processes, ganttChart, 0);
        calculateAverageTime();
        displayProcessInfo();
    }

    void refresh() {
        super.refresh();
        completedProcess = 0;
    }
}
