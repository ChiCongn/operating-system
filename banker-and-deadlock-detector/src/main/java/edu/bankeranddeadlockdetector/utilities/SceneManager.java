package edu.bankeranddeadlockdetector.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import edu.bankeranddeadlockdetector.Simulator;

import java.io.IOException;

public class SceneManager {

    public static final String BANKER_VIEW_PATH = "/edu/bankeranddeadlockdetector/banker.fxml";
    public static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SceneManager.primaryStage = primaryStage;
    }

    public static <Controller> Controller switchScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(Simulator.class.getResource(fxmlFilePath));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            primaryStage.setScene(newScene);
            primaryStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
