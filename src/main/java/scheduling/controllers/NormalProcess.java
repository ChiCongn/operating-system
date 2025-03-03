package scheduling.controllers;

import scheduling.utilities.Alert;
import scheduling.utilities.InputHandler;

public class NormalProcess extends CommonController {

    @Override
    void handleFileUpload() {
        InputHandler.handleFileUpload(filePath, processes);
    }

    @Override
    boolean validateInput() {
        if (processes.isEmpty()) {
            InputHandler.parseManualInput(processNames, arrivalTimes, burstTimes, processes);
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
}
