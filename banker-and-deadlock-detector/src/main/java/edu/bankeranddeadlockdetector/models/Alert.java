package edu.bankeranddeadlockdetector.models;

import edu.bankeranddeadlockdetector.AlertController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Alert {
    private static final String ALERT_FXML_PATH = "/edu/bankeranddeadlockdetector/alert.fxml";

    /**
     * Creates and displays an alert dialog.
     *
     * @param message   The message to display.
     * @param isConfirm If true, it's a confirmation dialog; otherwise, a notification.
     * @return true if confirmed, false otherwise.
     */
    private static boolean showAlert(String message, boolean isConfirm) {
        try {
            FXMLLoader loader = new FXMLLoader(Alert.class.getResource(ALERT_FXML_PATH));
            AnchorPane alertPane = loader.load();
            AlertController alertController = loader.getController();

            // Set the message and mode
            alertController.setAlertMessage(message);
            alertController.reset();
            if (!isConfirm) alertController.setNotificationMode();

            // Create & configure the alert stage
            Stage alertStage = new Stage();
            alertStage.setTitle(isConfirm ? "Confirmation" : "Notification");
            alertStage.setScene(new Scene(alertPane));
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setResizable(false);
            alertStage.showAndWait();

            // Return confirmation status if applicable
            return isConfirm && alertController.isUserConfirmed();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Alert FXML", e);
        }
    }

    /**
     * Displays a confirmation alert and waits for user interaction.
     *
     * @param message The message to display.
     * @return true if the user confirms, false otherwise.
     */
    public static boolean showConfirmation(String message) {
        return showAlert(message, true);
    }

    /**
     * Displays a notification alert.
     *
     * @param message The message to display.
     */
    public static void showNotification(String message) {
        showAlert(message, false);
    }
}
