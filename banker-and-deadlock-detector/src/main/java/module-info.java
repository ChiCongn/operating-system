module edu.bankeranddeadlockdetector {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens edu.bankeranddeadlockdetector to javafx.fxml;
    exports edu.bankeranddeadlockdetector;

    opens edu.bankeranddeadlockdetector.banker to javafx.fxml;
    exports edu.bankeranddeadlockdetector.banker;

    opens edu.bankeranddeadlockdetector.deadlockdetector to javafx.fxml;
    exports edu.bankeranddeadlockdetector.deadlockdetector;

    opens edu.bankeranddeadlockdetector.models to javafx.fxml;
    exports edu.bankeranddeadlockdetector.models;
}