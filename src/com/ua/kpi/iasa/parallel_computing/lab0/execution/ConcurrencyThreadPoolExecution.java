package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.MinThreadContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.CountThreadContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.*;
import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.printResult;

public class ConcurrencyThreadPoolExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_THREAD_POOL";
    private static final int EXECUTOR_SERVICE_SHUT_DOWN_LIMIT = 5;

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        int[] resultVector = calculateResultVector(runContext, ConcurrencyThreadPoolExecution::concurrencyThreadPool);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyThreadPoolExecution::concurrencyThreadPool, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, runContext.getNumberToFind(), resultVector[0]);
    }

    private static int concurrencyThreadPool(RunContext runContext) {
        int min = calculateMin(runContext);
        runContext.setNumberToFind(min);

        AtomicInteger atomicResult = calculateCount(runContext);

        return atomicResult.get();
    }

    private static int calculateMin(RunContext runContext) {
        AtomicInteger minResult = new AtomicInteger(runContext.getVector()[0]);
        List<MinThreadContext> minThreadContexts = generateMinThreadContextsBasedOnBatchSize(runContext, minResult, runContext.getBatchSizeThreadPool());

        // Create Executor Service
        int threadsCount = runContext.getThreadsCount();
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        // Submit tasks
        for (MinThreadContext minThreadContext : minThreadContexts) {
            Runnable runnable = generateMinRunnable(minThreadContext);
            executorService.submit(runnable);
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(EXECUTOR_SERVICE_SHUT_DOWN_LIMIT, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return minResult.get();
    }

    private static AtomicInteger calculateCount(RunContext runContext) {
        // Create threads contexts
        AtomicInteger countResult = new AtomicInteger();
        List<CountThreadContext> countThreadContexts = generateCountThreadContextsBasedOnBatchSize(runContext, countResult, runContext.getBatchSizeThreadPool());

        // Create Executor Service
        int threadsCount = runContext.getThreadsCount();
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        // Submit tasks
        for (CountThreadContext countThreadContext : countThreadContexts) {
            Runnable runnable = generateCountRunnable(countThreadContext);
            executorService.submit(runnable);
        }

        // Shutdown Executor Service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(EXECUTOR_SERVICE_SHUT_DOWN_LIMIT, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return countResult;
    }

    private ConcurrencyThreadPoolExecution() {
    }
}
