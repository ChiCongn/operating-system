package edu.bankeranddeadlockdetector.banker;

import edu.bankeranddeadlockdetector.models.Alert;
import edu.bankeranddeadlockdetector.models.Process;
import edu.bankeranddeadlockdetector.utilities.Format;
import edu.bankeranddeadlockdetector.utilities.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class BankerController {

    @FXML
    private Button increaseResource;

    @FXML
    private Button decreaseResource;

    @FXML
    private TextArea detailCalculationInfo;

    @FXML
    private TextField instanceResourceA;

    @FXML
    private TextField instanceResourceB;

    @FXML
    private TextField instanceResourceC;

    @FXML
    private TextField instanceResourceD;

    @FXML
    private TextField instanceResourceE;

    @FXML
    private TableView<Process> availableResource;

    @FXML
    private TableView<Process> processResource;

    @FXML
    private TableColumn<Process, String> allocation;

    @FXML
    private TableColumn<Process, String> max;

    @FXML
    private TableColumn<Process, String> need;

    @FXML
    private TableColumn<Process, String> processName;

    @FXML
    private Button reset;

    @FXML
    private Button addProcess;

    @FXML
    private Button addRequest;

    @FXML
    private Button simulate;

    @FXML
    private HBox resourceA;

    @FXML
    private HBox resourceB;

    @FXML
    private HBox resourceC;

    @FXML
    private HBox resourceD;

    @FXML
    private HBox resourceE;

    HBox[] resources;
    TextField[] instanceResources;
    int[] available;

    @FXML
    private Label safetyProcessSequence;

    private static final int MAX_RESOURCES = 5;

    private int numOfResources = 1;
    int[] totalInstanceResource;
    boolean valid;

    ObservableList<Process> processes = FXCollections.observableArrayList();


    @FXML
    void initialize() {
        processResource.setEditable(true);

        processName.setCellFactory(TextFieldTableCell.forTableColumn());
        processName.setOnEditCommit(event -> {
            event.getRowValue().setProcessName(event.getNewValue());
        });
        allocation.setCellFactory(TextFieldTableCell.forTableColumn());
        allocation.setOnEditCommit(event -> {
            event.getRowValue().setAllocation(Format.parseArray(event.getNewValue())); // Convert String to int[]
        });
        max.setCellFactory(TextFieldTableCell.forTableColumn());
        max.setOnEditCommit(event -> {
            event.getRowValue().setMax(Format.parseArray(event.getNewValue()));
        });

        processName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProcessName()));
        allocation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAllocationAsString()));
        max.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMaxAsString()));
        need.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNeedAsString()));

        processResource.setItems(processes);

        resources = new HBox[]{resourceA, resourceB, resourceC, resourceD, resourceE};
        instanceResources = new TextField[]{instanceResourceA, instanceResourceB, instanceResourceC, instanceResourceD, instanceResourceE};
        totalInstanceResource = new int[MAX_RESOURCES];

        increaseResource.setOnAction(event -> increaseResource());
        decreaseResource.setOnAction(event -> decreaseResource());
        simulate.setOnAction(event -> simulate());
        addProcess.setOnAction((event -> addBlankRow()));
    }

    void simulate() {
        valid = false;
        getInstanceOfResources();
        getInput();
    }

    void increaseResource() {
        if (numOfResources >= MAX_RESOURCES) {
            Alert.showNotification("Cannot increase: Maximum resource limit reached.");
            return;
        }
        numOfResources++;
        System.out.println("Resource increased. Current count: " + numOfResources);
        setVisibleResources();
    }

    void decreaseResource() {
        if (numOfResources <= 0) {
            Alert.showNotification("Cannot decrease: No resources available.");
            return;
        }
        numOfResources--;
        System.out.println("Resource decreased. Current count: " + numOfResources);
        setVisibleResources();
    }

    void setVisibleResources() {
        System.out.println("set visible resource");
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null) {
                resources[i].setVisible(i < numOfResources);
            }
        }
    }

    void reset() {
        processes.clear();
        safetyProcessSequence.setText("...");
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
                    return;
                }
                if (!Validation.isNaturalNumberArray(max)) {
                    Alert.showNotification("max must be natural array");
                    return;
                }
                if (!validateThenCreateProcess(name, allocation, max, newProcesses)) return;
            }
        }

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


    void getInstanceOfResources() {
        for (int i = 0; i < numOfResources; i++) {
            String inputText = instanceResources[i].getText();

            // Validate if the input is an integer
            if (!Validation.isInteger(inputText)) {
                Alert.showNotification("Invalid instance of resource " + (char) ('A' + i) + inputText);
                return;
            }

            int value = Integer.parseInt(inputText);

            if (Validation.isNaturalNumber(value)) {
                totalInstanceResource[i] = value;
                System.out.println("instance of resource[" + i + "]: " + value);
            } else {
                System.out.println("Input must be a natural number: " + inputText);
            }
        }
    }

    void validateInput() {

        if (!Validation.isValidTotalAllocation(processes, totalInstanceResource)) {
            Alert.showNotification("Error: The total allocated resources must not exceed the available instances.");
        }
    }

    void addBlankRow() {
        Process blankProcess = new Process("---", new int[]{0, 0, 0}, new int[]{0, 0, 0});
        processResource.getItems().add(blankProcess);
    }

}
