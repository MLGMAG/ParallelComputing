package com.ua.kpi.iasa.parallel_computing.lab0.utils;

import com.ua.kpi.iasa.parallel_computing.lab0.context.MinThreadContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.CountThreadContext;
import com.ua.kpi.iasa.parallel_computing.lab0.exception.IncorrectResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.DataExportUtils.addColumn;

public final class GeneralUtils {

    private GeneralUtils() {
    }

    public static int findMinElement(int[] vector, int startPosition, int endPosition) {
        int min = vector[0];
        for (int i : vector) {
            if (i < min)
                min = i;
        }

        return min;
    }

    public static int calculateNumberOccurrence(int[] vector, int startPosition, int endPosition, int numberToFind) {
        int occurrenceCount = 0;
        for (int i = startPosition; i < endPosition; i++) {
            if (vector[i] == numberToFind) {
                occurrenceCount++;
            }
        }
        return occurrenceCount;
    }

    public static int[] calculateResultVector(RunContext runContext, ToIntFunction<RunContext> function) {
        int iterationCount = runContext.getIterationCount();
        int[] results = new int[iterationCount];

        for (int i = 0; i < results.length; i++) {
            int result = function.applyAsInt(runContext);
            results[i] = result;
        }

        return results;
    }

    public static void checkResults(int[] results) {
        int firstResult = results[0];
        for (int result : results) {
            if (firstResult != result) throw new IncorrectResultException();
        }
    }

    public static long profileAvgTime(RunContext runContext, ToIntFunction<RunContext> function, String exportColumnTitle) {
        int iterationCount = runContext.getIterationCount();

        long[] longs = profileTime(runContext, function);
        addColumn(runContext.getExportFileName(), exportColumnTitle, longs);

        long timeSum = 0L;
        for (long aLong : longs) {
            timeSum += aLong;
        }

        return timeSum / iterationCount;
    }

    public static long[] profileTime(RunContext runContext, ToIntFunction<RunContext> function) {
        int iterationCount = runContext.getIterationCount();

        long[] times = new long[iterationCount];
        for (int i = 0; i < iterationCount; i++) {
            long start = System.nanoTime();

            function.applyAsInt(runContext);

            long end = System.nanoTime();
            long delta = end - start;
            times[i] = delta;
        }

        return times;
    }

    public static void printResult(String title, long avgTimeNs, int minResult, int count) {
        double avgTimeMs = avgTimeNs / 1e6;
        String messagePattern = "%s approach result: min element '%d', count %d, avg time: %f ms (%d ns)";
        String message = String.format(messagePattern, title, minResult, count, avgTimeMs, avgTimeNs);
        System.out.println(message);
    }

    public static List<CountThreadContext> generateCountThreadContextsBasedOnBatchSize(RunContext runContext, AtomicInteger atomicResult, int batchSize) {
        int vectorLength = runContext.getVector().length;

        int threadBatchSize = vectorLength / batchSize;

        List<CountThreadContext> countThreadContexts = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            int startPosition = i * threadBatchSize;
            int endPosition = startPosition + threadBatchSize;
            int numberToFind = runContext.getNumberToFind();
            int[] vector = runContext.getVector();

            CountThreadContext countThreadContext;
            if (i == batchSize - 1) {
                countThreadContext = new CountThreadContext(startPosition, vectorLength, numberToFind, vector, atomicResult);
            } else {
                countThreadContext = new CountThreadContext(startPosition, endPosition, numberToFind, vector, atomicResult);
            }
            countThreadContexts.add(countThreadContext);
        }

        return countThreadContexts;
    }

    public static List<MinThreadContext> generateMinThreadContextsBasedOnBatchSize(RunContext runContext, AtomicInteger atomicResult, int batchSize) {
        int vectorLength = runContext.getVector().length;

        int threadBatchSize = vectorLength / batchSize;

        List<MinThreadContext> minThreadContexts = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            int startPosition = i * threadBatchSize;
            int endPosition = startPosition + threadBatchSize;
            int[] vector = runContext.getVector();

            MinThreadContext minThreadContext;
            if (i == batchSize - 1) {
                minThreadContext = new MinThreadContext(startPosition, vectorLength, vector, atomicResult);
            } else {
                minThreadContext = new MinThreadContext(startPosition, endPosition, vector, atomicResult);
            }
            minThreadContexts.add(minThreadContext);
        }

        return minThreadContexts;
    }

    public static Runnable generateCountRunnable(CountThreadContext countThreadContext) {
        return () -> {
            int[] vector = countThreadContext.getVector();
            int startPosition = countThreadContext.getStartPosition();
            int endPosition = countThreadContext.getEndPosition();
            int numberToFind = countThreadContext.getNumberToFind();

            int result = calculateNumberOccurrence(vector, startPosition, endPosition, numberToFind);

            AtomicInteger numberCount = countThreadContext.getNumberCount();
            numberCount.addAndGet(result);
        };
    }

    public static Runnable generateMinRunnable(MinThreadContext minThreadContext) {
        return () -> {
            int[] vector = minThreadContext.getVector();
            int startPosition = minThreadContext.getStartPosition();
            int endPosition = minThreadContext.getEndPosition();

            int result = findMinElement(vector, startPosition, endPosition);

            AtomicInteger minNumber = minThreadContext.getMinNumber();

            lessThanCAS(minNumber, result);
        };
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
}
