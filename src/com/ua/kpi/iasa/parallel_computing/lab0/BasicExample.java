package com.ua.kpi.iasa.parallel_computing.lab0;

import com.ua.kpi.iasa.parallel_computing.lab0.context.RunContext;
import com.ua.kpi.iasa.parallel_computing.lab0.execution.ConcurrencyForkJoinPoolExecution;
import com.ua.kpi.iasa.parallel_computing.lab0.execution.ConcurrencyLowLevelExecution;
import com.ua.kpi.iasa.parallel_computing.lab0.execution.ConcurrencyThreadPoolExecution;
import com.ua.kpi.iasa.parallel_computing.lab0.execution.SequentialExecution;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.DataExportUtils.setupFile;

public class BasicExample {

    private static final int VECTOR_SIZE = 20_000_000;
    private static final int RANDOM_MAX_NUMBER = 1_000;
    private static final int RANDOM_MIN_NUMBER = 1;
    private static final int ITERATION_COUNT = 100;
    private static final int THREADS_COUNT = 4;
    private static final int BATCH_SIZE_THREAD_POOL = 2;
    private static final int BATCH_SIZE_FORK_JOIN_POOL = 10_000_000;
    private static final String EXPORT_FILE_NAME = "data.csv";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void main(String[] args) {
        RunContext runContext = createRunContext();
        printRunContext(runContext);

        setupFile(runContext);

        // Sequential approach
        SequentialExecution.sequentialApproach(runContext);

        // Concurrent low level approach
        ConcurrencyLowLevelExecution.concurrentApproach(runContext);

        // Concurrent thread pool approach
        ConcurrencyThreadPoolExecution.concurrentApproach(runContext);

        // Concurrent fork/join pool approach
        ConcurrencyForkJoinPoolExecution.concurrentApproach(runContext);
    }

    private static RunContext createRunContext() {
        int[] vector = generateVector(RANDOM_MIN_NUMBER, RANDOM_MAX_NUMBER, VECTOR_SIZE);
        List<Integer> arrayList = new ArrayList<>();
        for (int i : vector) {
            arrayList.add(i);
        }
        int numberToFind = RANDOM_MAX_NUMBER - 1;
        return new RunContext(ITERATION_COUNT, THREADS_COUNT, numberToFind, vector, arrayList, BATCH_SIZE_THREAD_POOL, BATCH_SIZE_FORK_JOIN_POOL, EXPORT_FILE_NAME);
    }

    private static int[] generateVector(int randMin, int randMax, int vectorSize) {
        int[] vector = new int[vectorSize];
        int delta = randMax - randMin;

        for (int i = 0; i < vector.length; i++) {
            int randomElement = randMin + (int) (delta * SECURE_RANDOM.nextFloat());
            vector[i] = randomElement;
        }
        return vector;
    }

    private static void printRunContext(RunContext runContext) {
        String messagePatter = "IterationCount: %d\nThreadsCount: %d\nNumberToFind: %d\nVectorSize: %d\nBatchSizeThreadPool: %d\nBatchSizeForkJoinPool: %d";
        String message = String.format(messagePatter, runContext.getIterationCount(), runContext.getThreadsCount(),
                runContext.getNumberToFind(), runContext.getVector().length, runContext.getBatchSizeThreadPool(),
                runContext.getBatchSizeForkJoinPool());
        System.out.println(message);
    }
}
