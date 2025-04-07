package edu.tool.controllers;

import edu.tool.algorithms.memory.*;
import edu.tool.enums.PageReplacementAlgorithm;
import edu.tool.utils.Alert;
import edu.tool.utils.InputHandler;
import edu.tool.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class PageReplacementController {
    @FXML
    GridPane grid;

    @FXML
    TextField frameSizeField;

    @FXML
    Label numberOfReferences;

    @FXML
    TextField referenceField;

    @FXML
    Label pageFaults;

    @FXML
    Label numberOfHits;

    @FXML
    Label hitRate;

    @FXML
    ChoiceBox<PageReplacementAlgorithm> pageReplacementAlgorithms;

    @FXML
    Button run;

    String[] references;
    int frameSize;

    @FXML
    void initialize() {
        pageReplacementAlgorithms.getItems().addAll(PageReplacementAlgorithm.values());
        pageReplacementAlgorithms.setValue(PageReplacementAlgorithm.FIFO);
        run.setOnAction(event -> run());
    }

    void getInputValue() {
        String input = referenceField.getText();
        references = InputHandler.convertStringToStringArray(input);
        frameSize = InputHandler.convertStringToInteger(frameSizeField.getText());
        if (frameSize <= 0) {
            Alert.showNotification("Invalid Frame Size", "Frame size must be greater than 0.");
        }
        System.out.println("input: " + input);
        System.out.println("frame size: " + frameSize);

    }

    void run() {
        // 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
        getInputValue();
        PageReplacementAlgorithm selectedAlgorithm = pageReplacementAlgorithms.getValue();
        grid.getChildren().clear();
        grid.setPrefHeight(50 * (frameSize + 1)); // sets preferred height to 400 pixels
        runPageReplacementAlgorithm(selectedAlgorithm);

        int pageFault = InputHandler.convertStringToInteger(pageFaults.getText());
        int numberOfReference = references.length;
        int numOfHits = numberOfReference - pageFault;
        numberOfHits.setText(Integer.toString(numOfHits));
        double hitRateValue = (double) numOfHits / numberOfReference * 100;
        hitRate.setText(String.format("%.2f", hitRateValue));
        numberOfReferences.setText(Integer.toString(numberOfReference));
    }

    void runPageReplacementAlgorithm(PageReplacementAlgorithm algorithm) {
        switch (algorithm) {
            case FIFO:
                FIFO.run(references, frameSize, pageFaults, grid);
                break;
            case LRU:
                LRU.run(references, frameSize, pageFaults, grid);
                break;
            case OPTIMAL:
                Optimal.run(references, frameSize, pageFaults, grid);
                break;
            case MRU:
                MRU.run(references, frameSize, pageFaults, grid);
                break;
            case LFU:
                LFU.run(references, frameSize, pageFaults, grid);
                break;
            case MFU:
                MFU.run(references, frameSize, pageFaults, grid);
                break;
            case SECOND_CHANCE:
                SecondChance.run(references, frameSize, pageFaults, grid);
                break;
            default:
                Alert.showNotification("Error", "Unknown algorithm");
                throw new UnsupportedOperationException("Unknown algorithm: " + algorithm);
        }
    }

    void switchToSchedulingView() {
        SchedulingController schedulingController = SceneSwitcher.switchScene(SceneSwitcher.SCHEDULING_VIEW_PATH);
    }
}
