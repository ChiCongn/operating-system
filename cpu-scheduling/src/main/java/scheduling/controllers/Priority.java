package scheduling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import scheduling.models.Process;
import scheduling.utilities.Alert;
import scheduling.utilities.InputHandler;

public class Priority extends CommonController {

    @FXML
    TextField priorities;

    @Override
    boolean validateInput() {
        if (processes.isEmpty()) {
            InputHandler.parseManualPriorityInput(processNames, arrivalTimes, burstTimes, priorities, processes);
        }

        if (processes.isEmpty()) {
            handleFileUpload();
        }

        // Final check if no processes are found
        if (processes.isEmpty()) {
            Alert.showAlert("Error", "No processes found! Please upload a file or add processes manually.");
            return false;
        }

        return true;
    }

    @Override
    void handleFileUpload() {
        InputHandler.handleFileUploadPriorityProcess(filePath, processes);
    }

    void refresh() {
        super.refresh();
        priorities.clear();

    }

    void displayProcessInfo() {
        super.displayProcessInfo();

        if (!processes.isEmpty()) {
            StringBuilder priority = new StringBuilder();
            for (Process p : processes) {
                priority.append(p.getBurstTime()).append(" ");
            }

            priorities.setText(priority.toString());
        } else {
            priorities.setText("No processes available");
        }
    }
}
