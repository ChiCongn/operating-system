package scheduling;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Simulator extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //SceneManager.setPrimaryStage(primaryStage);
        try {
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(Simulator.class.getResource("/scheduling/fcfs.fxml"));
            loader.setLocation(Simulator.class.getResource("/scheduling/fxml/nonpreemptive-sjf.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Simulator");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
