package edu.memorymanagement.controller;

import edu.memorymanagement.MemoryUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PagingController {

    @FXML
    TextField virtualMemorySizeField;
    @FXML
    TextField pageSizeField;
    @FXML
    TextField memoryUnitSizeField;
    @FXML
    TextField numberOfFramesField;
    @FXML
    TextField physicalAddressBitsField;

    @FXML
    Label pageNumberBitsLabel;
    @FXML
    Label frameNumberBitsLabel;

    @FXML
    Label pageTableSizeBitsLabel;
    @FXML
    Label pageTableSizeBytesLabel;
    @FXML
    Label pageTableSizeKBLabel;
    @FXML
    Label pageTableSizeMBLabel;

    @FXML
    Label invertedPageTableSizeBitsLabel;
    @FXML
    Label invertedPageTableSizeBytesLabel;
    @FXML
    Label invertedPageTableSizeKBLabel;
    @FXML
    Label invertedPageTableSizeMBLabel;

    @FXML
    Label mainMemorySizeBitsLabel;
    @FXML
    Label mainMemorySizeBytesLabel;
    @FXML
    Label mainMemorySizeKBLabel;
    @FXML
    Label mainMemorySizeMBLabel;

    @FXML
    ChoiceBox<MemoryUnit> virtualMemorySizeChoice;
    @FXML
    ChoiceBox<MemoryUnit> pageSizeChoice;
    @FXML
    ChoiceBox<MemoryUnit> memoryUnitSizeChoice;

    @FXML
    Button run;

    int virtualMemorySizeKB;
    int pageSizeKB;
    int memoryUnitSizeBytes;
    int numberOfFrames;
    int physicalAddressBits;

    @FXML
    public void initialize() {
        virtualMemorySizeChoice.getItems().addAll(MemoryUnit.values());
        pageSizeChoice.getItems().addAll(MemoryUnit.values());
        memoryUnitSizeChoice.getItems().addAll(MemoryUnit.values());

        virtualMemorySizeChoice.setValue(MemoryUnit.GB);
        pageSizeChoice.setValue(MemoryUnit.KB);
        memoryUnitSizeChoice.setValue(MemoryUnit.BYTE);

        virtualMemorySizeChoice.setOnAction(event -> {
            System.out.println("Virtual Memory Size: " + virtualMemorySizeChoice.getValue());
        });

        pageSizeChoice.setOnAction(event -> {
            System.out.println("Page Size: " + pageSizeChoice.getValue());
        });

        memoryUnitSizeChoice.setOnAction(event -> {
            System.out.println("Memory Unit Size: " + memoryUnitSizeChoice.getValue());
        });
        run.setOnAction(event -> run());
    }

    void run() {
        getInput();
        calculate();
    }

    void getInput() {
        virtualMemorySizeKB = getInputValue(virtualMemorySizeField, virtualMemorySizeChoice);
        pageSizeKB = getInputValue(pageSizeField, pageSizeChoice);
        memoryUnitSizeBytes = getInputValueSizeBytes(memoryUnitSizeField, memoryUnitSizeChoice);
        numberOfFrames = getInputValue(numberOfFramesField);
        physicalAddressBits = getInputValue(physicalAddressBitsField);
        System.out.printf("Virtual Memory Size (KB): %d%n", virtualMemorySizeKB);
        System.out.printf("Page Size (KB): %d%n", pageSizeKB);
        System.out.printf("Memory Unit Size (bytes): %d%n", memoryUnitSizeBytes);
        System.out.printf("Number of Frames: %d%n", numberOfFrames);
        System.out.printf("Physical Address Bits: %d%n", physicalAddressBits);

    }

    void calculate() {
        int numberOfPages = virtualMemorySizeKB / pageSizeKB;
        System.out.println("number of pages: " + numberOfPages);
        double numOfUnit = Math.pow(2, physicalAddressBits);
        System.out.println("number of units = " + numOfUnit);
        double mainMemorySize;
        if (physicalAddressBits != 0) {
            mainMemorySize = numOfUnit * memoryUnitSizeBytes / 1024;
            System.out.println("main memory calculated base on physical address bits = " + mainMemorySize);
            numberOfFrames = (int) mainMemorySize / pageSizeKB;
            System.out.println("number of frames: " + numberOfFrames);
            numberOfFramesField.setText(Integer.toString(numberOfFrames));
        } else {
            mainMemorySize = numberOfFrames * pageSizeKB;
        }

        pageNumberBitsLabel.setText(Integer.toString(calculateBits(numberOfPages)));
        frameNumberBitsLabel.setText(Integer.toString(calculateBits(numberOfFrames)));

        setMainMemorySizeLabels(mainMemorySize);
        int entry = calculateBits(numberOfPages) + calculateBits(numberOfFrames);
        System.out.println("entry = " + entry);
        double pageTableSizeKB = numberOfPages * entry;
        setPageTableSizeLabels(pageTableSizeKB);
        setInvertedPageTableSizeLabels(numberOfFrames * entry);
    }

    int getInputValue(TextField textField) {
        if (textField.getText().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị nhập không hợp lệ!");
            return 0;
        }
    }

    int getInputValue(TextField textField, ChoiceBox<MemoryUnit> choiceBox) {
        if (textField.getText().isEmpty()) {
            return 0;
        }
        try {
            int value = Integer.parseInt(textField.getText());
            MemoryUnit unit = choiceBox.getValue();
            return convertToKB(value, unit);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị nhập không hợp lệ!");
            return 0;
        }
    }

    int getInputValueSizeBytes(TextField textField, ChoiceBox<MemoryUnit> choiceBox) {
        if (textField.getText().isEmpty()) {
            return 0;
        }
        try {
            int value = Integer.parseInt(textField.getText());
            MemoryUnit unit = choiceBox.getValue();
            return convertToBytes(value, unit);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị nhập không hợp lệ!");
            return 0;
        }
    }

    int convertToBytes(int value, MemoryUnit unit) {
        return switch (unit) {
            case BYTE -> value;
            case KB -> value * 1024;
            case MB -> value * 1024 * 1024;
            case GB -> value * 1024 * 1024 * 1024;
        };
    }

    int convertToKB(int value, MemoryUnit unit) {
        return switch (unit) {
            case BYTE -> value / 1024;
            case KB -> value;
            case MB -> value * 1024;
            case GB -> value * 1024 * 1024;
        };
    }

    int calculateBits(int num) {
        return (int) Math.ceil(Math.log(num) / Math.log(2));
    }

    void setMainMemorySizeLabels(double mainMemorySizeKB) {
        double mainMemorySizeBits = mainMemorySizeKB * 1024 * 8;
        double mainMemorySizeBytes = mainMemorySizeKB * 1024;
        double mainMemorySizeMB = mainMemorySizeKB / 1024;

        mainMemorySizeBitsLabel.setText(String.format("%.0f", mainMemorySizeBits));
        mainMemorySizeBytesLabel.setText(String.format("%.0f", mainMemorySizeBytes));
        mainMemorySizeKBLabel.setText(String.format("%.0f", mainMemorySizeKB));
        mainMemorySizeMBLabel.setText(String.format("%.4f", mainMemorySizeMB));
    }

    void setPageTableSizeLabels(double pageTableSizeBits) {
        double pageTableSizeBytes = pageTableSizeBits / 8;
        double pageTableSizeKB = pageTableSizeBytes / 1024;
        double pageTableSizeMB = pageTableSizeKB / 1024;

        pageTableSizeBitsLabel.setText(String.format("%.0f", pageTableSizeBits));
        pageTableSizeBytesLabel.setText(String.format("%.4f", pageTableSizeBytes));
        pageTableSizeKBLabel.setText(String.format("%.4f", pageTableSizeKB));
        pageTableSizeMBLabel.setText(String.format("%.4f", pageTableSizeMB));
    }

    void setInvertedPageTableSizeLabels(double invertedPageTableSizeBits) {
        double invertedPageTableSizeBytes = invertedPageTableSizeBits / 8;
        double invertedPageTableSizeKB = invertedPageTableSizeBytes / 1024;
        double invertedPageTableSizeMB = invertedPageTableSizeKB / 1024;

        invertedPageTableSizeBitsLabel.setText(String.format("%.0f", invertedPageTableSizeBits));
        invertedPageTableSizeBytesLabel.setText(String.format("%.4f", invertedPageTableSizeBytes));
        invertedPageTableSizeKBLabel.setText(String.format("%.4f", invertedPageTableSizeKB));
        invertedPageTableSizeMBLabel.setText(String.format("%.4f", invertedPageTableSizeMB));
    }
}
