package edu.tool.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Drawer {
    public static final int UNIT_WIDTH = 30;
    public static final int BLOCK_HEIGHT = 30;
    public static final int POSITION_Y = 10;
    public static final double START_X = 20;
    static final Map<String, Color> processColors = new HashMap<>();
    private static int colorIndex = 0;

    // scale according to duration
    public static void drawProcessBlock(GraphicsContext gc, String processName, double startX,
                                        int startTime, int duration) {
        System.out.println("drawing process block, scaled based on its duration");

        gc.setFill(getColorForProcess(processName));
        gc.fillRect(startX, POSITION_Y, duration * UNIT_WIDTH, BLOCK_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, POSITION_Y, duration * UNIT_WIDTH, BLOCK_HEIGHT);

        // Display process name inside the block
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.fillText(processName, startX + (double) (duration * UNIT_WIDTH) / 2 - 10, POSITION_Y + 20);

        // Display time markers below the block
        gc.fillText(String.valueOf(startTime), startX, POSITION_Y + 50);
    }

    public static void drawProcessBlock(GraphicsContext gc, String processName, double startX, int startTime) {
        System.out.println("draw process block");

        gc.setFill(getColorForProcess(processName));
        gc.fillRect(startX, POSITION_Y, UNIT_WIDTH, BLOCK_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, POSITION_Y, UNIT_WIDTH, BLOCK_HEIGHT);

        // Display process name inside the block
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.fillText(processName, startX + (double) UNIT_WIDTH / 2 - 10, POSITION_Y + 20);

        // Display time markers below the block
        gc.fillText(String.valueOf(startTime), startX, POSITION_Y + 50);
    }

    // scale according to duration
    public static void drawIdleBlock(GraphicsContext gc, double startX, int idleStart, int idleDuration) {
        System.out.println("drawing idle block, scaled based on its duration");
        double width = idleDuration * UNIT_WIDTH;

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(startX, POSITION_Y, width, BLOCK_HEIGHT);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, POSITION_Y, width, BLOCK_HEIGHT);

        // Label the idle block
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(idleStart), startX, POSITION_Y + 50);
    }

    public static void drawIdleBlock(GraphicsContext gc, double startX, int idleStartTime) {
        System.out.println("draw idle block");

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(startX, POSITION_Y, UNIT_WIDTH, BLOCK_HEIGHT);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, POSITION_Y, UNIT_WIDTH, BLOCK_HEIGHT);

        // Label the idle block
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(idleStartTime), startX, POSITION_Y + 50);
    }

    public static void clearGanttChart(Canvas ganttChart) {
        GraphicsContext gc = ganttChart.getGraphicsContext2D();
        gc.clearRect(0, 0, ganttChart.getWidth(), ganttChart.getHeight());
    }

    public static void drawTheFirstRowGrid(String[] references, GridPane grid) {
        for (int i = 0; i < references.length; i++) {
            Label label = new Label(references[i]);
            label.getStyleClass().add("cell-page-replacement");
            label.getStyleClass().add("empty-cell");
            grid.add(label, i, 0);
        }
    }

    public static void drawColumnGrid(String[] reference, int index, int pageFaultIndex, GridPane grid) {
        for (int i = 0; i < reference.length; i++) {
            Label label = new Label(reference[i]);
            label.getStyleClass().add("cell-page-replacement");

            if (i == pageFaultIndex) {
                label.getStyleClass().add("page-fault");
            } else if (reference[i] != null && !reference[i].isEmpty()) {
                label.getStyleClass().add("page-in-frame");
            } else {
                label.getStyleClass().add("empty-cell");
            }

            grid.add(label, index, i + 1); // ignore first row
        }
    }

    static Color getColorForProcess(String processName) {
        if (!processColors.containsKey(processName)) {
            processColors.put(processName, generateNewColor());
        }
        return processColors.get(processName);
    }

    static Color generateNewColor() {
        double hue = (colorIndex * 137) % 360; // Generates distinct hues
        colorIndex++;
        return Color.hsb(hue, 0.7, 0.9);
    }

}
