package com.ua.kpi.iasa.parallel_computing.lab0.context;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadContext {
    private final int startPosition;
    private final int endPosition;
    private final int numberToFind;
    private final int[] vector;
    private final AtomicInteger numberCount;

    public ThreadContext(int startPosition, int endPosition, int numberToFind, int[] vector, AtomicInteger numberCount) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.numberToFind = numberToFind;
        this.vector = vector;
        this.numberCount = numberCount;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public int getNumberToFind() {
        return numberToFind;
    }

    public int[] getVector() {
        return vector;
    }

    public AtomicInteger getNumberCount() {
        return numberCount;
    }
}
