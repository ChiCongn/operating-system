package edu.bankeranddeadlockdetector;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertController {

    @FXML
    private Label alertMessage;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private boolean userConfirmed = false;


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

    public void setAlertMessage(String message) {
        alertMessage.setText(message);
    }

    public boolean isUserConfirmed() {
        return userConfirmed;
    }

    public void reset() {
        userConfirmed = false;
    }

    public void setNotificationMode() {
        cancelButton.setVisible(false);
        confirmButton.setLayoutX(alertMessage.getPrefWidth() / 2 - confirmButton.getPrefWidth() / 2);
    }

    private void closeDialog() {
        Stage stage = (Stage) alertMessage.getScene().getWindow();
        stage.close();
    }
}