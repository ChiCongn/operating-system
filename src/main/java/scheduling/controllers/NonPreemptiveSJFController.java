package scheduling.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduling.models.NonPreemptiveSJF;
import scheduling.models.Process;
import scheduling.utilities.GanttChartDrawer;

import java.util.*;

public class NonPreemptiveSJFController extends NormalProcess {
    @FXML
    public void initialize() {
        setCellValue();

        // Button Actions
        uploadFile.setOnAction(event -> handleFileUpload());
        startSimulation.setOnAction(event -> simulate());
        //addProcess.setOnAction(event -> InputHandler.addManualProcess(processes));
        refresh.setOnAction(event -> refresh());
    }

    private void simulate() {
        if (!validateInput()) return;

        NonPreemptiveSJF.simulate(processes, ganttChart);
        calculateAverageTime();
        displayProcessInfo();
    }
}
