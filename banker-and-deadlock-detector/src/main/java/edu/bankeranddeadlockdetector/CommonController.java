package edu.bankeranddeadlockdetector;

import edu.bankeranddeadlockdetector.banker.BankerController;
import edu.bankeranddeadlockdetector.deadlockdetector.DeadlockDetectorController;
import edu.bankeranddeadlockdetector.models.Alert;
import edu.bankeranddeadlockdetector.models.Banker;
import edu.bankeranddeadlockdetector.models.Process;
import edu.bankeranddeadlockdetector.utilities.Format;
import edu.bankeranddeadlockdetector.utilities.SceneManager;
import edu.bankeranddeadlockdetector.utilities.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CommonController {

    @FXML
    protected Button banker;

    @FXML
    protected Button deadlock;

    @FXML
    protected Button increaseResource;

    @FXML
    protected Button decreaseResource;

    @FXML
    protected TextArea detailCalculationInfo;

    @FXML
    protected TextField instanceResourceA;

    @FXML
    protected TextField instanceResourceB;

    @FXML
    protected TextField instanceResourceC;

    @FXML
    protected TextField instanceResourceD;

    @FXML
    protected TextField instanceResourceE;

    @FXML
    protected TableView<Process> processResource;

    @FXML
    protected TableColumn<Process, String> allocation;

    @FXML
    protected TableColumn<Process, String> processName;

    @FXML
    protected Button delete;

    @FXML
    protected Button addProcess;

    @FXML
    protected Button simulate;

    @FXML
    protected HBox resourceA;

    @FXML
    protected HBox resourceB;

    @FXML
    protected HBox resourceC;

    @FXML
    protected HBox resourceD;

    @FXML
    protected HBox resourceE;

    @FXML
    protected VBox availableResourcesA;

    @FXML
    protected VBox availableResourcesB;

    @FXML
    protected VBox availableResourcesC;

    @FXML
    protected VBox availableResourcesD;

    @FXML
    protected VBox availableResourcesE;

    @FXML
    protected Label availableResourceALabel;

    @FXML
    protected Label availableResourceBLabel;

    @FXML
    protected Label availableResourceCLabel;

    @FXML
    protected Label availableResourceDLabel;

    @FXML
    protected Label availableResourceELabel;

    protected HBox[] resources;
    protected TextField[] instanceResources;
    protected int[] totalInstanceResource;

    protected VBox[] availableResourcesVbox;
    protected Label[] availableResourcesLabel;
    protected int[] availableResources;

    @FXML
    protected Label safeProcessSequence;

    protected static final int MAX_RESOURCES = 5;

    protected int numOfResources = 1;

    protected boolean valid;

    protected ObservableList<Process> processes = FXCollections.observableArrayList();



    protected void initializeAttribute() {
        processResource.setEditable(true);

        processName.setCellFactory(TextFieldTableCell.forTableColumn());
        processName.setOnEditCommit(event -> {
            event.getRowValue().setProcessName(event.getNewValue());
        });
        allocation.setCellFactory(TextFieldTableCell.forTableColumn());
        allocation.setOnEditCommit(event -> {
            event.getRowValue().setAllocation(Format.parseArray(event.getNewValue())); // Convert String to int[]
        });

        processName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProcessName()));
        allocation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAllocationAsString()));

        processResource.setItems(processes);

        resources = new HBox[]{resourceA, resourceB, resourceC, resourceD, resourceE};
        instanceResources = new TextField[]{instanceResourceA, instanceResourceB, instanceResourceC, instanceResourceD, instanceResourceE};
        totalInstanceResource = new int[MAX_RESOURCES];

        availableResourcesVbox = new VBox[]{availableResourcesA, availableResourcesB, availableResourcesC, availableResourcesD, availableResourcesE};
        availableResourcesLabel = new Label[]{availableResourceALabel, availableResourceBLabel, availableResourceCLabel, availableResourceDLabel, availableResourceELabel};

        increaseResource.setOnAction(event -> increaseResource());
        decreaseResource.setOnAction(event -> decreaseResource());
        delete.setOnAction(event -> deleteSelectedRow());
    }

    public void switchToBanker() {
        BankerController bankerController = SceneManager.switchScene(SceneManager.BANKER_VIEW_PATH);
    }

    public void switchToDeadlockDetector() {
        DeadlockDetectorController deadlockDetectorController = SceneManager.switchScene(SceneManager.DEADLOCK_DETECTOR_VIEW_PATH);
    }

    protected void getInstanceOfResources() {
        for (int i = 0; i < numOfResources; i++) {
            String inputText = instanceResources[i].getText();

            // Validate if the input is an integer
            if (!Validation.isInteger(inputText)) {
                Alert.showNotification("Invalid instance of resource " + (char) ('A' + i) + inputText);
                valid = false;
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
        valid = true;
    }

    protected void increaseResource() {
        if (numOfResources >= MAX_RESOURCES) {
            edu.bankeranddeadlockdetector.models.Alert.showNotification("Cannot increase: Maximum resource limit reached.");
            return;
        }
        numOfResources++;
        System.out.println("Resource increased. Current count: " + numOfResources);
        setVisibleResources();
    }

    protected void decreaseResource() {
        if (numOfResources <= 0) {
            Alert.showNotification("Cannot decrease: No resources available.");
            return;
        }
        numOfResources--;
        System.out.println("Resource decreased. Current count: " + numOfResources);
        setVisibleResources();
    }

    protected void setVisibleResources() {
        System.out.println("set visible resource");
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null) {
                resources[i].setVisible(i < numOfResources);
            }
            availableResourcesLabel[i].setVisible(i < numOfResources);
            availableResourcesVbox[i].setVisible(i < numOfResources);
        }
    }

    protected void setTextForAvailableResourcesLabel() {
        for (int i = 0; i < numOfResources; i++) {
            availableResourcesLabel[i].setText(Integer.toString(availableResources[i]));
        }
    }

    protected void deleteSelectedRow() {
        Process selectedBook = processResource.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            processResource.getItems().remove(selectedBook);
        } else {
            Alert.showNotification("No row selected!");
        }
    }

    protected void validateInput() {
        availableResources = Banker.calculateAvailableResources(processes, totalInstanceResource, numOfResources);
        if (!Validation.isValidTotalAllocation(processes, totalInstanceResource, numOfResources)) {
            Alert.showNotification("Error: The total allocated resources must not exceed the available instances.");
            valid = false;
            return;
        }
        valid = true;
    }
}
