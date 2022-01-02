package com.ua.kpi.iasa.parallel_computing.lab0.context;

public class RunContext {
    private final int iterationCount;
    private final int threadsCount;
    private final int numberToFind;
    private final int[] vector;
    private final int batchSizeThreadPool;
    private final int batchSizeForkJoinPool;
    private final String exportFileName;

    public RunContext(int iterationCount, int threadsCount, int numberToFind, int[] vector, int batchSizeThreadPool, int batchSizeForkJoinPool, String exportFileName) {
        this.iterationCount = iterationCount;
        this.threadsCount = threadsCount;
        this.numberToFind = numberToFind;
        this.vector = vector;
        this.batchSizeThreadPool = batchSizeThreadPool;
        this.batchSizeForkJoinPool = batchSizeForkJoinPool;
        this.exportFileName = exportFileName;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public int getNumberToFind() {
        return numberToFind;
    }

    public int[] getVector() {
        return vector;
    }

    public int getBatchSizeThreadPool() {
        return batchSizeThreadPool;
    }

    public int getBatchSizeForkJoinPool() {
        return batchSizeForkJoinPool;
    }

    public String getExportFileName() {
        return exportFileName;
    }
}
