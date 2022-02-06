package com.ua.kpi.iasa.parallel_computing.lab1.execution;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab1.context.ThreadContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ua.kpi.iasa.parallel_computing.lab1.utils.GeneralUtils.*;

public final class ConcurrencyThreadPoolExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_THREAD_POOL";
    private static final int EXECUTOR_SERVICE_SHUT_DOWN_LIMIT = 5;

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        List<MinMaxResult> resultVector = calculateResultVector(runContext, ConcurrencyThreadPoolExecution::concurrencyThreadPool);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyThreadPoolExecution::concurrencyThreadPool, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, resultVector.get(0));
    }

    private static MinMaxResult concurrencyThreadPool(RunContext runContext) {
        // Create threads contexts
        Integer initialValue = runContext.getData().get(0).get(0);
        AtomicInteger atomicMaxResult = new AtomicInteger(initialValue);
        AtomicInteger atomicMinResult = new AtomicInteger(initialValue);
        List<ThreadContext> threadContexts = generateThreadContextsBasedOnBatchSize(runContext, atomicMaxResult, atomicMinResult, runContext.getBatchSizeThreadPool());

        // Create Executor Service
        int threadsCount = runContext.getThreadsCount();
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        // Submit tasks
        for (ThreadContext threadContext : threadContexts) {
            Runnable runnable = generateRunnable(threadContext);
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

        return new MinMaxResult(atomicMinResult.get(), atomicMaxResult.get());
    }


    private ConcurrencyThreadPoolExecution() {
    }
}
