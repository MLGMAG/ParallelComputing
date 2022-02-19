package com.ua.kpi.iasa.parallel_computing.lab0.execution;

import com.ua.kpi.iasa.parallel_computing.lab0.context.MinTaskContext;

import java.util.concurrent.RecursiveTask;

import static com.ua.kpi.iasa.parallel_computing.lab0.utils.GeneralUtils.findMinElement;

public class FindMinTask extends RecursiveTask<Integer> {

    private final MinTaskContext minTaskContext;

    public FindMinTask(MinTaskContext minTaskContext) {
        this.minTaskContext = minTaskContext;
    }

    @Override
    protected Integer compute() {
        int threshold = minTaskContext.getThreshold();
        int endIndex = minTaskContext.getEndIndex();
        int startIndex = minTaskContext.getStartIndex();
        int intervalLength = endIndex - startIndex;

        if (intervalLength <= threshold) {
            int[] vector = minTaskContext.getVector();
            return findMinElement(vector, startIndex, endIndex);
        }

        return splitTaskAndGetResult(startIndex, endIndex, intervalLength);
    }

    private int splitTaskAndGetResult(int startIndex, int endIndex, int intervalLength) {
        int intervalHalf = intervalLength / 2;
        MinTaskContext taskContext1 =
                new MinTaskContext(minTaskContext, startIndex, startIndex + intervalHalf);
        FindMinTask findMinTask = new FindMinTask(taskContext1);
        findMinTask.fork();

        MinTaskContext taskContext2 =
                new MinTaskContext(minTaskContext, startIndex + intervalHalf, endIndex);
        FindMinTask findMinTask1 = new FindMinTask(taskContext2);
        Integer result2 = findMinTask1.compute();
        Integer result1 = findMinTask.join();

        if (result1 < result2) {
            return result1;
        } else {
            return result2;
        }
    }

}
