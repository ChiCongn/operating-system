module edu.cpuscheduling {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports scheduling;
    opens scheduling to javafx.fxml;

    exports scheduling.controllers;
    opens scheduling.controllers to javafx.fxml;

    exports scheduling.models;
    opens scheduling.models to javafx.fxml;
}