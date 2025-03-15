package edu.bankeranddeadlockdetector.models;

import edu.bankeranddeadlockdetector.banker.AddRequestController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddRequest {

    private static final String ADD_REQUEST_VIEW_PATH = "/edu/bankeranddeadlockdetector/add-request.fxml";

    public static String[] showRequestDialogAndGetRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(AddRequest.class.getResource(ADD_REQUEST_VIEW_PATH));
            AnchorPane requestPane = loader.load();

            AddRequestController requestController = loader.getController();

            Stage requestStage = new Stage();
            requestStage.setTitle("Add Request");
            requestStage.setScene(new Scene(requestPane));
            requestStage.initModality(Modality.APPLICATION_MODAL);
            requestStage.setResizable(false);
            requestStage.showAndWait();

            return requestController.getRequestData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Add Request FXML", e);
        }
    }

}
