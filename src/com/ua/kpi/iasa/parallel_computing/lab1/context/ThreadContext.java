package com.ua.kpi.iasa.parallel_computing.lab1.context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadContext {
    private final int startVector;
    private final int endVector;
    private final List<List<Integer>> data;
    private final AtomicInteger maxValue;
    private final AtomicInteger minValue;

    public ThreadContext(int startVector, int endVector, List<List<Integer>> data, AtomicInteger maxValue, AtomicInteger minValue) {
        this.startVector = startVector;
        this.endVector = endVector;
        this.data = data;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public int getStartVector() {
        return startVector;
    }

    public int getEndVector() {
        return endVector;
    }

    public List<List<Integer>> getData() {
        return data;
    }

    public AtomicInteger getMaxValue() {
        return maxValue;
    }

    public AtomicInteger getMinValue() {
        return minValue;
    }
}
