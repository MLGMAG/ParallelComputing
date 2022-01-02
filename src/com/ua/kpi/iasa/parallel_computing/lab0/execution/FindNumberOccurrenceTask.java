package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.FindNumberOccurrenceTaskContext;

import java.util.concurrent.RecursiveTask;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.calculateNumberOccurrence;

public class FindNumberOccurrenceTask extends RecursiveTask<Integer> {
    private final FindNumberOccurrenceTaskContext findNumberOccurrenceTaskContext;

    public FindNumberOccurrenceTask(FindNumberOccurrenceTaskContext findNumberOccurrenceTaskContext) {
        this.findNumberOccurrenceTaskContext = findNumberOccurrenceTaskContext;
    }

    @Override
    protected Integer compute() {
        int threshold = findNumberOccurrenceTaskContext.getThreshold();
        int endIndex = findNumberOccurrenceTaskContext.getEndIndex();
        int startIndex = findNumberOccurrenceTaskContext.getStartIndex();
        int intervalLength = endIndex - startIndex;

        if (intervalLength <= threshold) {
            int[] vector = findNumberOccurrenceTaskContext.getVector();
            int numberToFind = findNumberOccurrenceTaskContext.getNumberToFind();
            return calculateNumberOccurrence(vector, startIndex, endIndex, numberToFind);
        }

        return splitTaskAndGetResult(startIndex, endIndex, intervalLength);
    }

    private int splitTaskAndGetResult(int startIndex, int endIndex, int intervalLength) {
        int intervalHalf = intervalLength / 2;
        FindNumberOccurrenceTaskContext taskContext1 =
                new FindNumberOccurrenceTaskContext(findNumberOccurrenceTaskContext, startIndex, startIndex + intervalHalf);
        FindNumberOccurrenceTask findNumberOccurrenceTask1 = new FindNumberOccurrenceTask(taskContext1);
        findNumberOccurrenceTask1.fork();

        FindNumberOccurrenceTaskContext taskContext2 =
                new FindNumberOccurrenceTaskContext(findNumberOccurrenceTaskContext, startIndex + intervalHalf, endIndex);
        FindNumberOccurrenceTask findNumberOccurrenceTask2 = new FindNumberOccurrenceTask(taskContext2);
        Integer result2 = findNumberOccurrenceTask2.compute();
        Integer result1 = findNumberOccurrenceTask1.join();

        return result1 + result2;
    }
}