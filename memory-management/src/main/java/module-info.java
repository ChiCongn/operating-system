module edu.memorymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens edu.memorymanagement to javafx.fxml;
    exports edu.memorymanagement;

    opens edu.memorymanagement.controller to javafx.fxml;
    exports edu.memorymanagement.controller;
}