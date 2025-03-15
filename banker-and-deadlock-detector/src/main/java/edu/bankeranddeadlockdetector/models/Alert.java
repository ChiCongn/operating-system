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

    static FXMLLoader fxmlLoader;
    static AnchorPane alertPane;
    static AlertController alertController;
    static Scene alertScene;

    // Static block to initialize the AlertDialog components
    static {
        try {
            System.out.println("initialize alert components");
            fxmlLoader = new FXMLLoader(Alert.class.getResource(ALERT_FXML_PATH));
            alertPane = fxmlLoader.load();
            alertController = fxmlLoader.getController();
            alertScene = new Scene(alertPane);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load AlertDialog FXML", e);
        }
    }

    /**
     * Displays a confirmation alert with a message and waits for user interaction.
     *
     * @param message The message to display in the alert.
     * @return true if the user confirms, false otherwise.
     */
    public static boolean showConfirmation(String message) {
        boolean isConfirmed = false;

        try {
            // Set the alert message and reset previous states
            alertController.setAlertMessage(message);
            alertController.reset();

            // Create and configure the alert stage
            Stage alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Confirmation");
            alertStage.setScene(alertScene);
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.showAndWait();

            // Get user confirmation status
            isConfirmed = alertController.isUserConfirmed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isConfirmed;
    }

    /**
     * Displays a notification alert with a message.
     *
     * @param message The message to display in the alert.
     */
    public static void showNotification(String message) {
        try {
            System.out.println("show notification!");
            // Set the alert message and configure as a notification
            alertController.setAlertMessage(message);
            alertController.reset();
            alertController.setNotificationMode();

            // Create and configure the alert stage
            Stage alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Notification");
            alertStage.setScene(alertScene);
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
