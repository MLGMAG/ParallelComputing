package com.ua.kpi.iasa.parallel_computing.lab0.context;

public class MinTaskContext {
    private final int[] vector;
    private final int startIndex;
    private final int endIndex;
    private final int threshold;

    public MinTaskContext(RunContext runContext) {
        this.threshold = runContext.getBatchSizeForkJoinPool();
        this.vector = runContext.getVector();
        this.startIndex = 0;
        this.endIndex = runContext.getVector().length;
    }

    public MinTaskContext(MinTaskContext minTaskContext, int startIndex, int endIndex) {
        this.vector = minTaskContext.getVector();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.threshold = minTaskContext.getThreshold();
    }

    public int[] getVector() {
        return vector;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getThreshold() {
        return threshold;
    }
}
