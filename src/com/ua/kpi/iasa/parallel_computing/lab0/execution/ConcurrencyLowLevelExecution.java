package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.ThreadContext;

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

        printResult(APPROACH_TITLE, avgTime, resultVector[0]);
    }

    private static int concurrencyLowLevel(RunContext runContext) {
        // Create threads contexts
        AtomicInteger atomicResult = new AtomicInteger();
        List<ThreadContext> threadContexts = generateThreadContextsBasedOnBatchSize(runContext, atomicResult, runContext.getThreadsCount());

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

        return atomicResult.get();
    }

    private ConcurrencyLowLevelExecution() {
    }
}
