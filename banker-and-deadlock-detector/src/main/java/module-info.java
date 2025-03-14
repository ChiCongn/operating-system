module edu.bankeranddeadlockdetector {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens edu.bankeranddeadlockdetector to javafx.fxml;
    exports edu.bankeranddeadlockdetector;
}