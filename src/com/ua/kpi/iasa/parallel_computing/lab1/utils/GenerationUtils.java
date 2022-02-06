package com.ua.kpi.iasa.parallel_computing.lab1.utils;

import com.ua.kpi.iasa.parallel_computing.lab1.context.MatrixGenerationContext;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GenerationUtils {

    private GenerationUtils() {
    }

    public static List<List<Integer>> generateMatrix(MatrixGenerationContext context) {
        int rowsCount = context.getRowsCount();
        return Stream.generate(() -> generateRow(context))
                .limit(rowsCount)
                .collect(Collectors.toList());
    }

    private static List<Integer> generateRow(MatrixGenerationContext context) {
        int columnsCount = context.getColumnsCount();

        return Stream.generate(() -> generateNumber(context))
                .limit(columnsCount)
                .collect(Collectors.toList());
    }

    private static int generateNumber(MatrixGenerationContext context) {
        SecureRandom secureRandom = context.getSecureRandom();
        int maxValue = context.getMaxValue();
        return (int) (maxValue * secureRandom.nextFloat()) + 1;
    }
}
