package com.ua.kpi.iasa.parallel_computing.lab0.context;

public class FindNumberOccurrenceTaskContext {
    private final int[] vector;
    private final int startIndex;
    private final int endIndex;
    private final int numberToFind;
    private final int threshold;

    public FindNumberOccurrenceTaskContext(RunContext runContext) {
        this.numberToFind = runContext.getNumberToFind();
        this.threshold = runContext.getBatchSizeForkJoinPool();

        this.vector = runContext.getVector();
        this.startIndex = 0;
        this.endIndex = runContext.getVector().length;
    }

    public FindNumberOccurrenceTaskContext(FindNumberOccurrenceTaskContext findNumberOccurrenceTaskContext, int startIndex, int endIndex) {
        this.vector = findNumberOccurrenceTaskContext.getVector();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.numberToFind = findNumberOccurrenceTaskContext.getNumberToFind();
        this.threshold = findNumberOccurrenceTaskContext.getThreshold();
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

    public int getNumberToFind() {
        return numberToFind;
    }

    public int getThreshold() {
        return threshold;
    }
}
