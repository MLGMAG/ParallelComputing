package com.ua.kpi.iasa.parallel_computing.lab2;

public class Context {
    private final Processor processor;
    private final Thread processorThread;
    private final Generator generator;
    private final Thread generatorThread;

    public Context(Processor processor, Generator generator) {
        this.processor = processor;
        this.processorThread = new Thread(processor);
        this.generator = generator;
        this.generatorThread = new Thread(generator);

        this.getProcessor().setContext(this);
        this.getGenerator().setContext(this);
    }

    public Processor getProcessor() {
        return processor;
    }

    public Thread getProcessorThread() {
        return processorThread;
    }

    public Generator getGenerator() {
        return generator;
    }

    public Thread getGeneratorThread() {
        return generatorThread;
    }
}
