package com.ua.kpi.iasa.parallel_computing.lab1.execution;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxResult;
import com.ua.kpi.iasa.parallel_computing.lab1.context.MinMaxTaskContext;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import static com.ua.kpi.iasa.parallel_computing.lab1.utils.GeneralUtils.calculateMinMax;

public class FindMinMaxTask extends RecursiveTask<MinMaxResult> {

    private final MinMaxTaskContext minMaxTaskContext;

    public FindMinMaxTask(MinMaxTaskContext minMaxTaskContext) {
        this.minMaxTaskContext = minMaxTaskContext;
    }

    @Override
    protected MinMaxResult compute() {
        int threshold = minMaxTaskContext.getThreshold();
        int startVector = minMaxTaskContext.getStartVector();
        int endVector = minMaxTaskContext.getEndVector();
        int intervalLength = endVector - startVector;

        if (intervalLength <= threshold) {
            List<List<Integer>> data = minMaxTaskContext.getData();
            return calculateMinMax(data, startVector, endVector);
        }

        return splitTaskAndGetResult(startVector, endVector, intervalLength);
    }

    private MinMaxResult splitTaskAndGetResult(int startVector, int endVector, int intervalLength) {
        int intervalHalf = intervalLength / 2;
        MinMaxTaskContext taskContext1 =
                new MinMaxTaskContext(minMaxTaskContext, startVector, startVector + intervalHalf);
        FindMinMaxTask findMinMaxTask = new FindMinMaxTask(taskContext1);
        findMinMaxTask.fork();

        MinMaxTaskContext taskContext2 =
                new MinMaxTaskContext(minMaxTaskContext, startVector + intervalHalf, endVector);
        FindMinMaxTask findMinMaxTask1 = new FindMinMaxTask(taskContext2);
        MinMaxResult result2 = findMinMaxTask1.compute();
        MinMaxResult result1 = findMinMaxTask.join();

        int min = Math.min(result1.getMin(), result2.getMin());
        int max = Math.max(result1.getMax(), result2.getMax());
        return new MinMaxResult(min, max);
    }
}
