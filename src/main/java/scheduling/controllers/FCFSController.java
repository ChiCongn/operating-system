package scheduling.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.FCFS;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.ArrayList;


public class FCFSController extends NormalProcess {
    @FXML
    public void initialize() {
        setCellValue();

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        //startSimulation.setOnAction(event -> runfcfsScheduling());
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void simulate() {
        if (!validateInput()) return;

        FCFS.simulate(processes, ganttChart, 0);
        calculateAverageTime();
        displayProcessInfo();
    }
}
