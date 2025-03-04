package scheduling.models;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import scheduling.utilities.GanttChartDrawer;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PreemptiveSJF {
    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int[] currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        currentTime[0] = Math.max(processes.getFirst().arrivalTime, currentTime[0]);

        double startX = 20;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));

        Process currentProcess = null;
        Process lastProcess = null;
        int lastProcessStartTime = 0;
        int processIndex = 0; // Track the next process to arrive

        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the ready queue
            while (processIndex < processes.size() && processes.get(processIndex).arrivalTime == currentTime[0]) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            // Re-add the running process if it's not finished
            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            // Pick the process with the shortest remaining time
            if (!readyQueue.isEmpty()) {
                Process nextProcess = readyQueue.poll();

                // If process changes, update Gantt chart
                if (currentProcess != nextProcess) {
                    if (lastProcess != null) {
                        GanttChartDrawer.drawColumn(
                                ganttChart.getGraphicsContext2D(),
                                lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
                        );
                        startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                    }
                    lastProcessStartTime = currentTime[0];
                }

                currentProcess = nextProcess;
                lastProcess = currentProcess;

                // Execute process for 1 time unit
                currentProcess.remainingTime--;

                // If process is finished
                if (currentProcess.remainingTime == 0) {
                    completedProcess++;
                    currentProcess.completionTime = currentTime[0] + 1;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                }
            }

            currentTime[0]++;
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(
                    ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime[0] - lastProcessStartTime
            );
            startX += (currentTime[0] - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display final time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime[0]), startX, GanttChartDrawer.POSITION_Y + 50);

        return completedProcess;
    }


    /*public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        double startX = 20;
        int totalProcesses = processes.size();

        Process lastProcess = null;
        int lastProcessStartTime = currentTime;
        int index = 0;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        readyQueue.add(processes.get(index++));

        while (index < totalProcesses || !readyQueue.isEmpty()) {
            Process currentProcess = readyQueue.poll();
            if (currentProcess == null) {
                // If CPU is idle, draw an idle block
                int nextArrival = processes.get(index).arrivalTime;
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, nextArrival - currentTime);

                currentTime = nextArrival;
                startX = currentTime * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block

                // Add new processes that have arrived
                while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                    readyQueue.add(processes.get(index++));
                }
                continue;
            }

            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // Preempt if necessary
            if (lastProcess != null && lastProcess != currentProcess) {
                GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                        lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime);
                startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                lastProcessStartTime = currentTime;
            }

            lastProcess = currentProcess;

            // Execute process for 1 time unit
            currentProcess.remainingTime--;

            // If process completes, update metrics
            if (currentProcess.remainingTime == 0) {
                completedProcess++;
                currentProcess.completionTime = currentTime + 1;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                // Re-add to queue if not finished
                readyQueue.add(currentProcess);
            }

            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }
        }

        // Draw final segment
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime);
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);
        return completedProcess;
    }*/

    public static int simulate(ObservableList<Process> processes, Canvas ganttChart, int completedProcess, int currentTime) {
        System.out.println("Simulating Preemptive Shortest Job First (SJF)");

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        currentTime = Math.max(processes.getFirst().arrivalTime, currentTime);

        int index = 0, totalProcesses = processes.size();
        double startX = 20;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        readyQueue.add(processes.get(index++));

        Process lastProcess = readyQueue.peek();
        int lastProcessStartTime = currentTime;

        while (completedProcess < totalProcesses) {
            if (readyQueue.isEmpty()) {
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
                    );
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime;
                lastProcess = null;
                // If CPU is idle, draw an idle block
                int nextArrival = processes.get(index).arrivalTime;
                System.out.println("next arrival time " + nextArrival);
                GanttChartDrawer.drawIdleBlock(ganttChart.getGraphicsContext2D(), startX, currentTime, nextArrival - currentTime);

                System.out.println("current time " + currentTime);
                System.out.println("startX " + startX);
                //startX += (nextArrival - currentTime) * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block
                currentTime = nextArrival;
                startX = currentTime * GanttChartDrawer.UNIT_WIDTH; // Move startX for next block

                // Add new processes that have arrived
                while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                    readyQueue.add(processes.get(index++));
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }

            // If process changes, update Gantt chart
            if (currentProcess != lastProcess) {
                if (lastProcess != null) {
                    GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                            lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
                    );
                    startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
                }
                lastProcessStartTime = currentTime;
                lastProcess = currentProcess;
            }

            // Execute process for 1 time unit quantum
            currentTime++;
            currentProcess.remainingTime--;


            /*// Execute for the time quantum = 1
            int executionTime = 1;
            currentProcess.remainingTime -= executionTime;

            // Draw execution block
            GanttChartDrawer.drawColumn(ganttChart.getGraphicsContext2D(),
                    currentProcess.getName(), startX, currentTime, executionTime);
            currentTime += executionTime;

            startX += executionTime * GanttChartDrawer.UNIT_WIDTH;*/


            while (index < totalProcesses && processes.get(index).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(index++));
            }

            // If process finishes execution
            if (currentProcess.remainingTime == 0) {
                completedProcess++;
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            } else {
                readyQueue.add(currentProcess); // Re-add the process if it's not finished
            }
        }

        // Draw the last segment of the Gantt chart
        if (lastProcess != null) {
            GanttChartDrawer.drawColumn(
                    ganttChart.getGraphicsContext2D(),
                    lastProcess.name, startX, lastProcessStartTime, currentTime - lastProcessStartTime
            );
            startX += (currentTime - lastProcessStartTime) * GanttChartDrawer.UNIT_WIDTH;
        }

        // Display total execution time
        ganttChart.getGraphicsContext2D().fillText(String.valueOf(currentTime), startX, GanttChartDrawer.POSITION_Y + 50);

        return completedProcess;
    }


}
