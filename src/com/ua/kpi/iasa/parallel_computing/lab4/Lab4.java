package com.ua.kpi.iasa.parallel_computing.lab4;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lab4 {

    private static final int VECTOR_SIZE = 20_000_000;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void main(String[] args) {
        System.out.println("Creating executor service...");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("Executor service is created!\n");

        System.out.println("Generating data...");
        List<Double> vector1 = generateData(VECTOR_SIZE);
        List<Double> vector2 = generateData(VECTOR_SIZE);
        System.out.println("Generating data is completed!\n");

        CompletableFuture<List<Double>> vector1Future = CompletableFuture
                .supplyAsync(() -> vector1.stream().filter(x -> x > 0.7).sorted().collect(Collectors.toList()), executorService);

        CompletableFuture<List<Double>> vector2Future = CompletableFuture
                .supplyAsync(() -> vector2.stream().filter(x -> x % 3 == 0).sorted().collect(Collectors.toList()), executorService);

        CompletableFuture<List<Double>> mergeResultsFuture = vector1Future
                .thenCombineAsync(vector2Future, (a, b) -> Stream.of(a, b).flatMap(List::stream).sorted().collect(Collectors.toList()), executorService);

        mergeResultsFuture.thenAcceptAsync(Lab4::printResults, executorService);

        System.out.println("Main thread is completed!");
//        shutDownExecutorService(executorService);
    }

    private static void shutDownExecutorService(ExecutorService executorService) {
        System.out.println("Start executor service shut down process...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private static void printResults(List<Double> result) {
        System.out.println("Result is calculated successfully!");
        System.out.println("Result size: " + result.size());
//        System.out.println("Result: " + result);
    }

    private static List<Double> generateData(int vectorSize) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < vectorSize; i++) {
            list.add(SECURE_RANDOM.nextDouble());
        }
        return list;
    }
}
