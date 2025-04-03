package edu.tool.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertController {

    @FXML
    Label message;

    @FXML
    Label title;

    @FXML
    Button confirmButton;

    @FXML
    Button cancelButton;

    boolean userConfirmed = false;


    @FXML
    public void initialize() {
        confirmButton.setOnAction(event -> {
            userConfirmed = true;
            closeDialog();
        });

        cancelButton.setOnAction(event -> {
            userConfirmed = false;
            closeDialog();
        });
    }

    public void setAlertContent(String title, String message) {
        this.title.setText(title);
        this.message.setText(message);
    }

    public boolean isUserConfirmed() {
        return userConfirmed;
    }

    public void reset() {
        userConfirmed = false;
    }

    public void setNotificationMode() {
        cancelButton.setVisible(false);
        confirmButton.setLayoutX(message.getPrefWidth() / 2 - confirmButton.getPrefWidth() / 2);
    }

    private void closeDialog() {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.close();
    }
}
