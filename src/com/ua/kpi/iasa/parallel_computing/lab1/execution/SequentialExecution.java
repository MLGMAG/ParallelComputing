package com.ua.kpi.iasa.parallel_computing.lab1.execution;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;

import java.util.List;

import static com.ua.kpi.iasa.parallel_computing.lab1.utils.GeneralUtils.*;

public final class SequentialExecution {

    private static final String APPROACH_TITLE = "SEQUENTIAL";

    public static void sequentialExecution(RunContext runContext) {
        // Result verification
        List<MinMaxResult> results = calculateResultVector(runContext, SequentialExecution::execute);
        checkResults(results);

        // Time profiling
        long avgTime = profileAvgTime(runContext, SequentialExecution::execute, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, results.get(0));
    }

    private static MinMaxResult execute(RunContext runContext) {
        List<List<Integer>> data = runContext.getData();
        return calculateMinMax(data, 0, data.size());
    }

    private SequentialExecution() {
    }
}
