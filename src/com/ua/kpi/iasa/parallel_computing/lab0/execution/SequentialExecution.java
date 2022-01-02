package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.*;

public class SequentialExecution {
    private static final String APPROACH_TITLE = "SEQUENTIAL";

    public static void sequentialApproach(RunContext runContext) {
        // Result verification
        int[] resultVector = calculateResultVector(runContext, SequentialExecution::sequential);
        checkResults(resultVector);

        // Time profiling
        long avgTime = profileAvgTime(runContext, SequentialExecution::sequential, APPROACH_TITLE);

        printResult(APPROACH_TITLE, avgTime, resultVector[0]);
    }

    private static int sequential(RunContext runContext) {
        int[] vector = runContext.getVector();
        int numberToFind = runContext.getNumberToFind();
        return calculateNumberOccurrence(vector, 0, vector.length, numberToFind);
    }

    private SequentialExecution() {
    }
}
