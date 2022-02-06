package com.ua.kpi.iasa.parallel_computing.lab1.context;

import java.util.List;

public final class MinMaxTaskContext {
    private final List<List<Integer>> data;
    private final int startVector;
    private final int endVector;
    private final int threshold;

    public MinMaxTaskContext(RunContext runContext) {
        this.data = runContext.getData();
        this.threshold = runContext.getBatchSizeForkJoinPool();
        this.startVector = 0;
        this.endVector = runContext.getData().size();
    }

    public MinMaxTaskContext(MinMaxTaskContext minMaxTaskContext, int startVector, int endVector) {
        this.data = minMaxTaskContext.getData();
        this.threshold = minMaxTaskContext.getThreshold();
        this.startVector = startVector;
        this.endVector = endVector;
    }

    public List<List<Integer>> getData() {
        return data;
    }

    public int getStartVector() {
        return startVector;
    }

    public int getEndVector() {
        return endVector;
    }

    public int getThreshold() {
        return threshold;
    }
}
