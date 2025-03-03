package scheduling;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scheduling.utilities.SceneManager;

import java.io.IOException;


public class Simulator extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        SceneManager.setPrimaryStage(primaryStage);
        try {
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(Simulator.class.getResource("/scheduling/fcfs.fxml"));
            //loader.setLocation(Simulator.class.getResource("/scheduling/fxml/non-preemptive-sjf.fxml"));
            loader.setLocation(Simulator.class.getResource(SceneManager.MULTILEVEL_QUEUE));
            //loader.setLocation(Simulator.class.getResource(SceneManager.ROUND_ROBIN));
            //loader.setLocation(Simulator.class.getResource(SceneManager.FCFS_PATH));
            //loader.setLocation(Simulator.class.getResource("/scheduling/fxml/round-robin.fxml"));
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
