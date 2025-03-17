package edu.bankeranddeadlockdetector.deadlockdetector;

import edu.bankeranddeadlockdetector.CommonController;
import edu.bankeranddeadlockdetector.models.Alert;
import edu.bankeranddeadlockdetector.models.DeadlockDetector;
import edu.bankeranddeadlockdetector.models.Process;
import edu.bankeranddeadlockdetector.utilities.Format;
import edu.bankeranddeadlockdetector.utilities.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;

public class DeadlockDetectorController extends CommonController {
    @FXML
    private TableColumn<Process, String> request;

    @FXML
    void initialize() {
        super.initializeAttribute();
        request.setCellFactory(TextFieldTableCell.forTableColumn());
        request.setOnEditCommit(event -> {
            event.getRowValue().setRequest(Format.parseArray(event.getNewValue()));
        });
        request.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRequestAsString()));

        simulate.setOnAction(event -> simulate());
        addProcess.setOnAction((event -> addBlankRow()));
        banker.setOnAction(event -> switchToBanker());
    }

    void simulate() {
        detailCalculationInfo.setText("----");
        safeProcessSequence.setText("--------");
        valid = true;
        getInstanceOfResources();
        getInput();
        validateInput();
        if (!valid) return;

        StringBuilder executionDetail = new StringBuilder();
        StringBuilder safeProgress = new StringBuilder();
        DeadlockDetector.simulateDeadlockDetector(processes, availableResources, executionDetail, safeProgress);
        setTextForAvailableResourcesLabel();
        detailCalculationInfo.setText(executionDetail.toString());
        safeProcessSequence.setText(safeProgress.toString());
    }

    void getInput() {
        List<Process> newProcesses = new ArrayList<>();

        for (Process rowData : processResource.getItems()) {
            if (rowData.getProcessName() != null && !rowData.getProcessName().isEmpty()) {
                String name = rowData.getProcessName();
                int[] allocation = rowData.getAllocation();
                int[] requestResource = rowData.getRequest();

                if (!Validation.isNaturalNumberArray(allocation)) {
                    Alert.showNotification("allocation must be natural array");
                    valid = false;
                    return;
                }
                if (!Validation.isNaturalNumberArray(requestResource)) {
                    Alert.showNotification("request must be natural array");
                    valid = false;
                    return;
                }
                if (!validateThenCreateProcess(name, allocation, requestResource, newProcesses)) return;
            }
        }
        valid = true;
        processes.setAll(newProcesses); // Efficient way to update the list without clearing it
        System.out.println("All processes added. Total: " + processes.size());
    }

    boolean validateThenCreateProcess(String name, int[] allocation, int[] request, List<Process> newProcesses) {
        if (allocation != null && request != null) {
            if (allocation.length != request.length || allocation.length != numOfResources) {
                Alert.showNotification("Error: Mismatched allocation/request sizes or incorrect number of resources.");
                return false;
            }

            Process newProcess = new Process(name, allocation, request, true);
            newProcesses.add(newProcess);
        } else {
            System.out.println("Skipping process: " + name + " due to missing data.");
        }
        return true;
    }

    void addBlankRow() {
        Process blankProcess = new Process("process name", new int[]{0, 0, 0}, new int[]{0, 0, 0}, true);
        processResource.getItems().add(blankProcess);
    }

}
