package com.ua.kpi.iasa.parallel_computing.lab2;

public class Lab2 {
    public static void main(String[] args) {
        Processor processor = new Processor();
        Generator generator = new Generator();

        Context context = new Context(processor, generator);

        context.getGeneratorThread().start();
        context.getProcessorThread().start();
    }
}
