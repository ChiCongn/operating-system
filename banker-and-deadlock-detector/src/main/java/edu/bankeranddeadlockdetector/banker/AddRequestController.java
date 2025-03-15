package edu.bankeranddeadlockdetector.banker;


import edu.bankeranddeadlockdetector.Simulator;
import edu.bankeranddeadlockdetector.models.Alert;
import edu.bankeranddeadlockdetector.utilities.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class AddRequestController {

    @FXML
    private Button confirmButton;

    @FXML
    private TextField processName;

    @FXML
    private TextField request;

    private boolean userConfirmed = false;

    public String[] getRequestData() {
        try {
            String name = processName.getText();
            String requestResources = request.getText();
            return new String[]{name, requestResources};

        } catch (NumberFormatException e) {
            Alert.showNotification( "Invalid Input! Please enter valid numbers.");
        }
        return null;
    }

    @FXML
    public void initialize() {
        confirmButton.setOnAction(event -> {
            userConfirmed = true;
            closeDialog();
        });
    }

    public boolean isUserConfirmed() {
        return userConfirmed;
    }

    public void reset() {
        userConfirmed = false;
    }

    private void closeDialog() {
        Stage stage = (Stage) processName.getScene().getWindow();
        stage.close();
    }

}

