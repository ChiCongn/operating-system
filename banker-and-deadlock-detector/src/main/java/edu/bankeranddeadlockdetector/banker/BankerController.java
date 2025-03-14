package edu.bankeranddeadlockdetector.banker;

import edu.bankeranddeadlockdetector.models.Process;
import edu.bankeranddeadlockdetector.utilities.Format;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

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
    private TableView<Process> available;

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

    @FXML
    private Label safetyProcessSequence;

    private static final int MAX_RESOURCES = 5;

    private int numOfResources = 0;
    int[] totalInstanceResource;

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
            event.getRowValue().setAllocation(Format.parseArray(event.getNewValue()));
        });

        //processResource.setItems(processes);

        resources = new HBox[]{resourceA, resourceB, resourceC, resourceD, resourceE};
        instanceResources = new TextField[]{instanceResourceA, instanceResourceB, instanceResourceC, instanceResourceD, instanceResourceE};
        totalInstanceResource = new int[MAX_RESOURCES];

        increaseResource.setOnAction(event -> increaseResource());
        decreaseResource.setOnAction(event -> decreaseResource());
        simulate.setOnAction(event -> simulate());
        addProcess.setOnAction((event -> addBlankRow()));
    }

    void simulate() {
        getInput();
    }

    void increaseResource() {
        if (numOfResources >= MAX_RESOURCES) {
            System.out.println("Cannot increase: Maximum resource limit reached.");
            return;
        }
        numOfResources++;
        System.out.println("Resource increased. Current count: " + numOfResources);
        setVisibleResources();
    }

    void decreaseResource() {
        if (numOfResources <= 0) {
            System.out.println("Cannot decrease: No resources available.");
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
        processes.clear(); // Clear the existing list before adding new data

        for (Process rowData : processResource.getItems()) {
            if (rowData.getProcessName() != null && !rowData.getProcessName().isEmpty()) {
                Process newProcess = new Process(
                        rowData.getProcessName(),
                        Format.parseArray(rowData.getAllocationAsString()),
                        Format.parseArray(rowData.getMaxAsString())
                );
                System.out.println("process name: " + rowData.getProcessName());
                processes.add(newProcess);
            }
        }
        System.out.println("All processes added. Total: " + processes.size());
    }

    void getInstanceOfResources() {
        for (int i = 0; i < numOfResources; i++) {

        }
    }

    boolean validateInput() {
        return false;
    }

    void addBlankRow() {
        Process blankProcess = new Process("", new int[]{0, 0, 0}, new int[]{0, 0, 0});
        processResource.getItems().add(blankProcess);
    }

}
