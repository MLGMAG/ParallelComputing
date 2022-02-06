package com.ua.kpi.iasa.parallel_computing.lab1.context;

import java.security.SecureRandom;

public final class MatrixGenerationContext {
    private final int rowsCount;
    private final int columnsCount;
    private final int maxValue;
    private final SecureRandom secureRandom = new SecureRandom();

    public MatrixGenerationContext(int rowsCount, int columnsCount, int maxValue) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.maxValue = maxValue;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public SecureRandom getSecureRandom() {
        return secureRandom;
    }
}
