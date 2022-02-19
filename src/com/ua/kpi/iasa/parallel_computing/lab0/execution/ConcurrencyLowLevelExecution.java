package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.MinThreadContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.CountThreadContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.*;

public class ConcurrencyLowLevelExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_LOW_LEVEL";

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        int[] resultVector = calculateResultVector(runContext, ConcurrencyLowLevelExecution::concurrencyLowLevel);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyLowLevelExecution::concurrencyLowLevel, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, runContext.getNumberToFind(), resultVector[0]);
    }

    private static int concurrencyLowLevel(RunContext runContext) {
        int minElement = calculateMinElement(runContext);
        runContext.setNumberToFind(minElement);

        AtomicInteger countAtomicResult = calculateCount(runContext);
        return countAtomicResult.get();
    }

    private static int calculateMinElement(RunContext runContext) {
        // Create min threads contexts
        AtomicInteger minAtomicResult = new AtomicInteger(runContext.getVector()[0]);
        List<MinThreadContext> minThreadContexts = generateMinThreadContextsBasedOnBatchSize(runContext, minAtomicResult, runContext.getThreadsCount());

        int threadsCount = runContext.getThreadsCount();
        Thread[] threads = new Thread[threadsCount];
        for (int j = 0; j < threads.length; j++) {
            Runnable runnable = generateMinRunnable(minThreadContexts.get(j));
            threads[j] = new Thread(runnable);
        }

        // Start threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Join
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        int minElement = minAtomicResult.get();
        return minElement;
    }

    private static AtomicInteger calculateCount(RunContext runContext) {
        // Create count threads contexts
        AtomicInteger countAtomicResult = new AtomicInteger();
        List<CountThreadContext> countThreadContexts = generateCountThreadContextsBasedOnBatchSize(runContext, countAtomicResult, runContext.getThreadsCount());

        // Create threads based on their context
        int threadsCount = runContext.getThreadsCount();
        Thread[] threads = new Thread[threadsCount];
        for (int j = 0; j < threads.length; j++) {
            Runnable runnable = generateCountRunnable(countThreadContexts.get(j));
            threads[j] = new Thread(runnable);
        }

        // Start threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Join
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return countAtomicResult;
    }

    private ConcurrencyLowLevelExecution() {
    }
}
