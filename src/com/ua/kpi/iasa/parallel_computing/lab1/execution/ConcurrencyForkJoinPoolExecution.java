package com.ua.kpi.iasa.parallel_computing.lab1.execution;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxTaskContext;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.ua.kpi.iasa.parallel_computing.lab1.utils.GeneralUtils.*;

public final class ConcurrencyForkJoinPoolExecution {

    private static final String APPROACH_TITLE = "CONCURRENT_FORK_JOIN_POOL";

    public static void concurrentApproach(RunContext runContext) {
        // Result verification
        List<MinMaxResult> resultVector = calculateResultVector(runContext, ConcurrencyForkJoinPoolExecution::concurrencyForkJoinPool);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, ConcurrencyForkJoinPoolExecution::concurrencyForkJoinPool, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, resultVector.get(0));
    }

    private static MinMaxResult concurrencyForkJoinPool(RunContext runContext) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        MinMaxTaskContext minMaxTaskContext = new MinMaxTaskContext(runContext);
        FindMinMaxTask findMinMaxTask = new FindMinMaxTask(minMaxTaskContext);
        return forkJoinPool.invoke(findMinMaxTask);
    }

    private ConcurrencyForkJoinPoolExecution() {
    }

}
