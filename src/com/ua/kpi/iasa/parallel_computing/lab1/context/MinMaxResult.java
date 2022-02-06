package com.ua.kpi.iasa.parallel_computing.lab1.context;

public final class MinMaxResult {
    private final int min;
    private final int max;

    public MinMaxResult(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinMaxResult that = (MinMaxResult) o;

        if (min != that.min) return false;
        return max == that.max;
    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        return result;
    }
}
