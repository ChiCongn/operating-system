package scheduling.utilities;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scheduling.models.Process;

public class GanttChartDrawer {
    public static final int UNIT_WIDTH = 25;
    public static final int BLOCK_HEIGHT = 30;
    public static final int POSITION_Y = 10;
    private static final Map<String, Color> processColors = new HashMap<>();
    private static int colorIndex = 0;


    public static void drawColumn(GraphicsContext gc, String processName, double startX, int startTime, int duration) {

        gc.setFill(getColorForProcess(processName));
        gc.fillRect(startX, POSITION_Y, duration * UNIT_WIDTH, BLOCK_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, POSITION_Y, duration * UNIT_WIDTH, BLOCK_HEIGHT);

        // Display process name inside the block
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.fillText(processName, startX + (duration * UNIT_WIDTH) / 2 - 10, POSITION_Y + 20);

        // Display time markers below the block
        gc.fillText(String.valueOf(startTime), startX, POSITION_Y + 50);

        System.out.println("draw process cell");
    }

    public static void drawColumn(GraphicsContext gc, String processName, double startX, double positionY, int startTime, int duration) {
        System.out.println("draw process cell");

        gc.setFill(getColorForProcess(processName));
        gc.fillRect(startX, positionY, duration * UNIT_WIDTH, BLOCK_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(startX, positionY, duration * UNIT_WIDTH, BLOCK_HEIGHT);

        // Display process name inside the block
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(16));
        gc.fillText(processName, startX + (duration * UNIT_WIDTH) / 2 - 10, POSITION_Y + 15);

        // Display time markers below the block
        gc.fillText(String.valueOf(startTime), startX, positionY + 45);

    }

    public static void draw(Canvas ganttChartCanvas, List<Process> processes) {
        GraphicsContext gc = ganttChartCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, ganttChartCanvas.getWidth(), ganttChartCanvas.getHeight()); // Clear previous drawing
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        double startX = 20;
        double positionY = 40;

        int currentTime = 0;

        for (Process process : processes) {
            int burstTime = process.getBurstTime();
            gc.setFill(Color.LIGHTBLUE);

            gc.fillRect(startX, positionY, burstTime * UNIT_WIDTH, 30); // Draw process execution bar
            gc.strokeRect(startX, positionY, burstTime * UNIT_WIDTH, 30); // Border

            // Display process name
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(18));
            gc.fillText(process.getName(), startX + (burstTime * UNIT_WIDTH) / 2 - 10, positionY + 20);

            // Display time markers
            gc.fillText(String.valueOf(currentTime), startX, positionY + 50);
            currentTime += burstTime;
            startX += burstTime * UNIT_WIDTH;
        }

        // Draw the final time marker
        gc.fillText(String.valueOf(currentTime), startX, positionY + 50);
    }

    public static void clearGanttChart(Canvas ganttChart) {
        GraphicsContext gc = ganttChart.getGraphicsContext2D();
        gc.clearRect(0, 0, ganttChart.getWidth(), ganttChart.getHeight()); // Clears the entire canvas
    }

    private static Color getColorForProcess(String processName) {
        if (!processColors.containsKey(processName)) {
            processColors.put(processName, generateNewColor());
        }
        return processColors.get(processName);
    }

    private static Color generateNewColor() {
        double hue = (colorIndex * 137) % 360; // Generates distinct hues
        colorIndex++;
        return Color.hsb(hue, 0.7, 0.9);
    }

}

