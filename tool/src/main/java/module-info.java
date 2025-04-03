module edu.tool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens edu.tool to javafx.fxml;
    exports edu.tool;

    opens edu.tool.controllers to javafx.fxml;
    exports edu.tool.controllers;
}