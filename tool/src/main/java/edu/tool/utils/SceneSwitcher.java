package edu.tool.utils;

import edu.tool.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    public static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SceneSwitcher.primaryStage = primaryStage;
    }

    public static <Controller> Controller switchScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFilePath));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            primaryStage.setScene(newScene);
            primaryStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during scene switch");
            return null;
        }
    }
}
