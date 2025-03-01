module edu.cpuscheduling {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens scheduling.cpuscheduling to javafx.fxml;
    exports scheduling.cpuscheduling;
}