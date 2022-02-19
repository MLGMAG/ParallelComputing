package com.ua.kpi.iasa.parallel_computing.lab0.context;

import java.util.concurrent.atomic.AtomicInteger;

public class MinThreadContext {
    private final int startPosition;
    private final int endPosition;
    private final int[] vector;
    private final AtomicInteger minNumber;

    public MinThreadContext(int startPosition, int endPosition, int[] vector, AtomicInteger minNumber) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.vector = vector;
        this.minNumber = minNumber;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public int[] getVector() {
        return vector;
    }

    public AtomicInteger getMinNumber() {
        return minNumber;
    }
}
