package com.ua.kpi.iasa.parallel_computing.lab1.context;

import java.util.List;

public final class RunContext {
    private final List<List<Integer>> data;
    private final int iterationCount;
    private final int threadsCount;
    private final int batchSizeThreadPool;
    private final int batchSizeForkJoinPool;
    private final String exportFileName;

    public RunContext(List<List<Integer>> data, int iterationCount, int threadsCount, int batchSizeThreadPool, int batchSizeForkJoinPool, String exportFileName) {
        this.data = data;
        this.iterationCount = iterationCount;
        this.threadsCount = threadsCount;
        this.batchSizeThreadPool = batchSizeThreadPool;
        this.batchSizeForkJoinPool = batchSizeForkJoinPool;
        this.exportFileName = exportFileName;
    }

    public List<List<Integer>> getData() {
        return data;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getThreadsCount() {
        return threadsCount;
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
