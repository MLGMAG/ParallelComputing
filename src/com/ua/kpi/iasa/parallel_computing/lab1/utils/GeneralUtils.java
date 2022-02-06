package com.ua.kpi.iasa.parallel_computing.lab1.utils;


import com.ua.kpi.iasa.parallel_computing.lab0.exception.IncorrectResultException;
import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab1.context.ThreadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.DataExportUtils.addColumn;

public final class GeneralUtils {

    private GeneralUtils() {
    }

    public static List<MinMaxResult> calculateResultVector(RunContext runContext, Function<RunContext, MinMaxResult> function) {
        int iterationCount = runContext.getIterationCount();
        List<MinMaxResult> results = new ArrayList<>();

        for (int i = 0; i < iterationCount; i++) {
            MinMaxResult result = function.apply(runContext);
            results.add(result);
        }

        return results;
    }

    public static void checkResults(List<MinMaxResult> results) {
        MinMaxResult firstResult = results.get(0);
        for (MinMaxResult result : results) {
            if (!firstResult.equals(result)) {
                throw new IncorrectResultException();
            }
        }
    }

    public static long profileAvgTime(RunContext runContext, Function<RunContext, MinMaxResult> function, String exportColumnTitle) {
        int iterationCount = runContext.getIterationCount();

        long[] longs = profileTime(runContext, function);
        addColumn(runContext.getExportFileName(), exportColumnTitle, longs);

        long timeSum = 0L;
        for (long aLong : longs) {
            timeSum += aLong;
        }

        return timeSum / iterationCount;
    }

    public static long[] profileTime(RunContext runContext, Function<RunContext, MinMaxResult> function) {
        int iterationCount = runContext.getIterationCount();

        long[] times = new long[iterationCount];
        for (int i = 0; i < iterationCount; i++) {
            long start = System.nanoTime();

            function.apply(runContext);

            long end = System.nanoTime();
            long delta = end - start;
            times[i] = delta;
        }

        return times;
    }

    public static List<ThreadContext> generateThreadContextsBasedOnBatchSize(RunContext runContext, AtomicInteger atomicMaxResult, AtomicInteger atomicMinResult, int batchSize) {
        int vectorsCount = runContext.getData().size();

        int threadBatchSize = vectorsCount / batchSize;

        List<ThreadContext> threadContexts = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            int startVector = i * threadBatchSize;
            int endVector = startVector + threadBatchSize;
            List<List<Integer>> data = runContext.getData();

            ThreadContext threadContext;
            if (i == batchSize - 1) {
                threadContext = new ThreadContext(startVector, vectorsCount, data, atomicMaxResult, atomicMinResult);
            } else {
                threadContext = new ThreadContext(startVector, endVector, data, atomicMaxResult, atomicMinResult);
            }
            threadContexts.add(threadContext);
        }

        return threadContexts;
    }

    public static Runnable generateRunnable(ThreadContext threadContext) {
        return () -> {
            List<List<Integer>> data = threadContext.getData();
            int startVector = threadContext.getStartVector();
            int endVector = threadContext.getEndVector();

            MinMaxResult result = calculateMinMax(data, startVector, endVector);

            AtomicInteger oldMaxValue = threadContext.getMaxValue();
            greaterThanCAS(oldMaxValue, result.getMax());

            AtomicInteger oldMinValue = threadContext.getMinValue();
            lessThanCAS(oldMinValue, result.getMin());
        };
    }

    public static MinMaxResult calculateMinMax(List<List<Integer>> data, int startVector, int endVector) {
        List<MinMaxResult> results = new ArrayList<>();
        for (int i = startVector; i < endVector; i++) {
            List<Integer> vector = data.get(i);
            MinMaxResult result = calculateMinMax(vector);
            results.add(result);
        }

        List<Integer> vector = resultToVector(results);
        return calculateMinMax(vector);
    }

    public static List<Integer> resultToVector(List<MinMaxResult> results) {
        List<Integer> vector = new ArrayList<>();

        results.forEach(result -> {
            vector.add(result.getMax());
            vector.add(result.getMin());
        });

        return vector;
    }

    public static MinMaxResult calculateMinMax(List<Integer> vector) {
        int max = vector.get(0);
        int min = vector.get(0);

        for (int value : vector) {
            if (value > max) {
                max = value;
            }

            if (value < min) {
                min = value;
            }
        }

        return new MinMaxResult(min, max);
    }

    public static void greaterThanCAS(AtomicInteger oldValue, int newValue) {
        while (true) {
            int local = oldValue.get();

            if (newValue < local) {
                return;
            }

            if (oldValue.compareAndSet(local, newValue)) {
                return;
            }
        }
    }

    public static void lessThanCAS(AtomicInteger oldValue, int newValue) {
        while (true) {
            int local = oldValue.get();

            if (newValue > local) {
                return;
            }

            if (oldValue.compareAndSet(local, newValue)) {
                return;
            }
        }
    }

    public static void printResult(String title, long avgTimeNs, MinMaxResult result) {
        double avgTimeMs = avgTimeNs / 1e6;
        String messagePattern = "%s approach result: min -> '%d' and max -> '%d', avg time: %f ms (%d ns)";
        String message = String.format(messagePattern, title, result.getMin(), result.getMax(), avgTimeMs, avgTimeNs);
        System.out.println(message);
    }
}
