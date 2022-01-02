package com.ua.kpi.iasa.parallel_computing.lab0.utils;

import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.ThreadContext;
import com.ua.kpi.iasa.parallel_computing.lab0.exception.IncorrectResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.DataExportUtils.addColumn;

public final class GeneralUtils {

    private GeneralUtils() {
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

    public static void printResult(String title, long avgTimeNs, int result) {
        double avgTimeMs = avgTimeNs / 1e6;
        String messagePattern = "%s approach result: %d, avg time: %f ms (%d ns)";
        String message = String.format(messagePattern, title, result, avgTimeMs, avgTimeNs);
        System.out.println(message);
    }

    public static List<ThreadContext> generateThreadContextsBasedOnBatchSize(RunContext runContext, AtomicInteger atomicResult, int batchSize) {
        int vectorLength = runContext.getVector().length;

        int threadBatchSize = vectorLength / batchSize;

        List<ThreadContext> threadContexts = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            int startPosition = i * threadBatchSize;
            int endPosition = startPosition + threadBatchSize;
            int numberToFind = runContext.getNumberToFind();
            int[] vector = runContext.getVector();

            ThreadContext threadContext;
            if (i == batchSize - 1) {
                threadContext = new ThreadContext(startPosition, vectorLength, numberToFind, vector, atomicResult);
            } else {
                threadContext = new ThreadContext(startPosition, endPosition, numberToFind, vector, atomicResult);
            }
            threadContexts.add(threadContext);
        }

        return threadContexts;
    }

    public static Runnable generateRunnable(ThreadContext threadContext) {
        return () -> {
            int[] vector = threadContext.getVector();
            int startPosition = threadContext.getStartPosition();
            int endPosition = threadContext.getEndPosition();
            int numberToFind = threadContext.getNumberToFind();

            int result = calculateNumberOccurrence(vector, startPosition, endPosition, numberToFind);

            AtomicInteger numberCount = threadContext.getNumberCount();
            numberCount.addAndGet(result);
        };
    }
}
