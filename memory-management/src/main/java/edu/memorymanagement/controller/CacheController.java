package edu.memorymanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CacheController {

    @FXML
    TextField cacheAccessTimeField;

    @FXML
    TextField memoryAccessTimeField;

    @FXML
    TextField cacheHitRateField;

    @FXML
    Label avgMemoryAccessTimeLabel;

    @FXML
    Label effectiveMemoryAccessTimeLabel;

    @FXML
    Button run;

    int cacheAccessTime;
    int memoryAccessTime;
    double cacheHitRate;

    @FXML
    void initialize() {
        run.setOnAction(event -> run());
    }

    void getInputValues() {
        try {
            cacheAccessTime = Integer.parseInt(cacheAccessTimeField.getText().trim());
            memoryAccessTime = Integer.parseInt(memoryAccessTimeField.getText().trim());
            cacheHitRate = Double.parseDouble(cacheHitRateField.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    void performCalculation() {
        double avgMemoryAccessTime = cacheHitRate * cacheAccessTime
                + (1 - cacheHitRate) * memoryAccessTime;
        double effectiveMemoryAccessTime = cacheHitRate * (cacheAccessTime + memoryAccessTime)
                + (1 - cacheHitRate) * (cacheAccessTime + memoryAccessTime * 2);

        avgMemoryAccessTimeLabel.setText(Double.toString(avgMemoryAccessTime));
        effectiveMemoryAccessTimeLabel.setText(Double.toString(effectiveMemoryAccessTime));
    }

    void run() {
        getInputValues();
        performCalculation();
    }

}
