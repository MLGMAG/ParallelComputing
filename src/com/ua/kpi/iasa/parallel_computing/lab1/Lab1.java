package com.ua.kpi.iasa.parallel_computing.lab1;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MatrixGenerationContext;
import com.ua.kpi.iasa.parallel_computing.lab1.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab1.execution.ConcurrencyForkJoinPoolExecution;
import com.ua.kpi.iasa.parallel_computing.lab1.execution.ConcurrencyLowLevelExecution;
import com.ua.kpi.iasa.parallel_computing.lab1.execution.ConcurrencyThreadPoolExecution;
import com.ua.kpi.iasa.parallel_computing.lab1.execution.SequentialExecution;
import com.ua.kpi.iasa.parallel_computing.lab1.utils.GenerationUtils;

import java.util.List;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.DataExportUtils.setupFile;

public class Lab1 {

    private static final int ROWS_COUNT = 100_000;
    private static final int COLUMNS_COUNT = 1_000;
    private static final int RANDOM_MAX_NUMBER = 100_000_000;
    private static final int ITERATION_COUNT = 100;
    private static final int THREADS_COUNT = 4;
    private static final int BATCH_SIZE_THREAD_POOL = 100;
    private static final int BATCH_SIZE_FORK_JOIN_POOL = 100;
    private static final String EXPORT_FILE_NAME = "data.csv";

    public static void main(String[] args) {
        RunContext runContext = generateRunContext();

        setupFile(runContext);

        SequentialExecution.sequentialExecution(runContext);

        ConcurrencyLowLevelExecution.concurrentApproach(runContext);

        ConcurrencyThreadPoolExecution.concurrentApproach(runContext);

        ConcurrencyForkJoinPoolExecution.concurrentApproach(runContext);
    }

    private static MatrixGenerationContext generateContext() {
        return new MatrixGenerationContext(ROWS_COUNT, COLUMNS_COUNT, RANDOM_MAX_NUMBER);
    }

    private static RunContext generateRunContext() {
        MatrixGenerationContext context = generateContext();
        List<List<Integer>> data = GenerationUtils.generateMatrix(context);
        return new RunContext(data, ITERATION_COUNT, THREADS_COUNT, BATCH_SIZE_THREAD_POOL, BATCH_SIZE_FORK_JOIN_POOL, EXPORT_FILE_NAME);
    }
}
