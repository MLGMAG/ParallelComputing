package com.ua.kpi.iasa.parallel_computing.lab1.execution;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab1.context.ThreadContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ua.kpi.iasa.parallel_computing.lab1.utils.GeneralUtils.*;


public final class ConcurrencyLowLevelExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_LOW_LEVEL";

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        List<MinMaxResult> resultVector = calculateResultVector(runContext, ConcurrencyLowLevelExecution::concurrencyLowLevel);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyLowLevelExecution::concurrencyLowLevel, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, resultVector.get(0));
    }

    private static MinMaxResult concurrencyLowLevel(RunContext runContext) {
        // Create threads contexts
        Integer initialValue = runContext.getData().get(0).get(0);
        AtomicInteger atomicMaxResult = new AtomicInteger(initialValue);
        AtomicInteger atomicMinResult = new AtomicInteger(initialValue);
        List<ThreadContext> threadContexts = generateThreadContextsBasedOnBatchSize(runContext, atomicMaxResult, atomicMinResult, runContext.getThreadsCount());

        // Create threads based on their context
        int threadsCount = runContext.getThreadsCount();
        Thread[] threads = new Thread[threadsCount];
        for (int j = 0; j < threads.length; j++) {
            Runnable runnable = generateRunnable(threadContexts.get(j));
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

        return new MinMaxResult(atomicMinResult.get(), atomicMaxResult.get());
    }

    private ConcurrencyLowLevelExecution() {
    }
}
