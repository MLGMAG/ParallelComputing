package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.FindNumberOccurrenceTaskContext;
import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;

import java.util.concurrent.ForkJoinPool;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.*;

public class ConcurrencyForkJoinPoolExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_FORK_JOIN_POOL";

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        int[] resultVector = calculateResultVector(runContext, ConcurrencyForkJoinPoolExecution::concurrencyForkJoinPool);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyForkJoinPoolExecution::concurrencyForkJoinPool, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, resultVector[0]);
    }

    private static int concurrencyForkJoinPool(RunContext runContext) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        FindNumberOccurrenceTaskContext findNumberOccurrenceTaskContext = new FindNumberOccurrenceTaskContext(runContext);
        FindNumberOccurrenceTask findNumberOccurrenceTask = new FindNumberOccurrenceTask(findNumberOccurrenceTaskContext);
        return forkJoinPool.invoke(findNumberOccurrenceTask);
    }

    private ConcurrencyForkJoinPoolExecution() {
    }
}
