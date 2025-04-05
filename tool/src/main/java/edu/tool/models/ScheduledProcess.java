package edu.tool.models;

public class ScheduledProcess {
    //int id; // simple so use process name to identify
    String name;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int priority;
    int queueLevel;
    int startTime = -1;
    int finishTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime = -1;
    boolean isCompleted = false;


    public ScheduledProcess(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public ScheduledProcess(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public ScheduledProcess(String name, int arrivalTime, int burstTime, int priority, int queueLevel) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.queueLevel = queueLevel;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" +
                ", name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", remainingTime=" + remainingTime +
                ", priority=" + priority +
                ", queueLevel=" + queueLevel +
                ", startTime=" + startTime +
                ", completionTime=" + finishTime +
                ", turnaroundTime=" + turnaroundTime +
                ", waitingTime=" + waitingTime +
                ", responseTime=" + responseTime +
                ", isCompleted=" + isCompleted +
                '}';
    }

    public void decrementRemainingTime(int amount) {
        if (remainingTime > 0) {
            remainingTime -= amount;
        }
    }


    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQueueLevel() {
        return queueLevel;
    }

    public void setQueueLevel(int queueLevel) {
        this.queueLevel = queueLevel;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
