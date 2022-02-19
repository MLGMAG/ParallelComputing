package com.ua.kpi.iasa.parallel_computing.lab2;

public class Process {
    private final int timeToComplete;
    private final int id;

    public Process(int timeToComplete, int id) {
        this.timeToComplete = timeToComplete;
        this.id = id;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    public int getId() {
        return id;
    }
}
