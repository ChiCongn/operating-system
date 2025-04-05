package edu.tool.controllers;

import edu.tool.algorithms.memory.*;
import edu.tool.enums.PageReplacementAlgorithm;
import edu.tool.utils.InputHandler;
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
    ChoiceBox<PageReplacementAlgorithm> pageReplacementAlgorithms;

    @FXML
    Button run;

    String[] references;
    int frameSize;

    @FXML
    void initialize() {
        pageReplacementAlgorithms.getItems().addAll(PageReplacementAlgorithm.values());
        run.setOnAction(event -> run());
    }

    void getInputValue() {
        String input = referenceField.getText();
        references = InputHandler.convertStringToStringArray(input);
        frameSize = InputHandler.convertStringToInteger(frameSizeField.getText());
        System.out.println("input: " + input);
        System.out.println("frame size: " + frameSize);
        numberOfReferences.setText(Integer.toString(frameSize));
    }

    void run() {
        // 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
        getInputValue();
        PageReplacementAlgorithm selectedAlgorithm = pageReplacementAlgorithms.getValue();
        runPageReplacementAlgorithm(selectedAlgorithm);
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
                throw new UnsupportedOperationException("Unknown algorithm: " + algorithm);
        }
    }
}
