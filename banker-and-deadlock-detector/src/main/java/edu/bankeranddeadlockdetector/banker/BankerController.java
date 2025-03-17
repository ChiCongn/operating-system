package edu.bankeranddeadlockdetector.banker;

import edu.bankeranddeadlockdetector.CommonController;
import edu.bankeranddeadlockdetector.models.AddRequest;
import edu.bankeranddeadlockdetector.models.Alert;
import edu.bankeranddeadlockdetector.models.Banker;
import edu.bankeranddeadlockdetector.models.Process;
import edu.bankeranddeadlockdetector.utilities.Format;
import edu.bankeranddeadlockdetector.utilities.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;

public class BankerController extends CommonController {

    @FXML
    private TableColumn<Process, String> max;

    @FXML
    private TableColumn<Process, String> need;

    @FXML
    private Button addRequest;


    @FXML
    void initialize() {
        super.initializeAttribute();
        max.setCellFactory(TextFieldTableCell.forTableColumn());
        max.setOnEditCommit(event -> {
            event.getRowValue().setMax(Format.parseArray(event.getNewValue()));
        });

        max.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaxAsString()));
        need.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNeedAsString()));

        simulate.setOnAction(event -> simulate());
        delete.setOnAction(event -> deleteSelectedRow());
        addRequest.setOnAction(event -> addRequest());
        deadlock.setOnAction(event -> switchToDeadlockDetector());
        addProcess.setOnAction(event -> addBlankRow());
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
        Banker.simulateBanker(processes, availableResources, executionDetail, safeProgress);
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
                int[] max = rowData.getMax();

                if (!Validation.isNaturalNumberArray(allocation)) {
                    Alert.showNotification("allocation must be natural array");
                    valid = false;
                    return;
                }
                if (!Validation.isNaturalNumberArray(max)) {
                    Alert.showNotification("max must be natural array");
                    valid = false;
                    return;
                }
                if (!validateThenCreateProcess(name, allocation, max, newProcesses)) return;
            }
        }
        valid = true;
        processes.setAll(newProcesses); // Efficient way to update the list without clearing it
        System.out.println("All processes added. Total: " + processes.size());
    }

    boolean validateThenCreateProcess(String name, int[] allocation, int[] max, List<Process> newProcesses) {
        if (allocation != null && max != null) {
            if (allocation.length != max.length || allocation.length != numOfResources) {
                Alert.showNotification("Error: Mismatched allocation/max sizes or incorrect number of resources.");
                return false;
            }
            if (!Validation.isValidAllocation(allocation, max)) {
                Alert.showNotification("Error: Allocation must not exceed the maximum allowed resources!");
                return false;
            }

            Process newProcess = new Process(name, allocation, max);
            newProcesses.add(newProcess);
        } else {
            System.out.println("Skipping process: " + name + " due to missing data.");
        }
        return true;
    }

    void addBlankRow() {
        Process blankProcess = new Process("---", new int[]{0, 0, 0}, new int[]{0, 0, 0});
        processResource.getItems().add(blankProcess);
    }

    void addRequest() {
        String[] requestData = AddRequest.showRequestDialogAndGetRequest();

        System.out.println("add request");
        if (requestData == null || requestData.length < 2) {
            Alert.showNotification("No valid request received.");
            valid = false;
            return;
        }

        String processName = requestData[0];
        int[] requestResource = Format.parseArray(requestData[1]); // Convert to int array
        if (!Validation.isValidAllocation(requestResource, availableResources)) {
            valid = false;
            Alert.showNotification("Error: The total request resources must not exceed the available instances.");
            return;
        }

        for (Process process : processes) {
            if (process.getProcessName().equals(processName)) {
                int[] newAllocation = process.getAllocation();

                // Add requestData to allocation element-wise
                for (int i = 0; i < newAllocation.length; i++) {
                    newAllocation[i] += requestResource[i];
                }

                process.setAllocation(newAllocation);
                processResource.refresh();
                break;
            }
        }
        valid = true;

    }

}
