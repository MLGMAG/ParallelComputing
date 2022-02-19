package com.ua.kpi.iasa.parallel_computing.lab2;

public class Generator implements Runnable {

    private static final int TIME_TO_SLEEP = 6000;
    private static final double TASK_COUNT = 5;

    private Context context;

    private int processId = 0;
    private double sentTasksCount = 0;
    private double removedTasks = 0;

    @Override
    public void run() {
        try {
            waitUntilProcessorIsReady();
            for (int i = 0; i < TASK_COUNT; i++) {
                work();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("[Generator] All tasks are sent!");

        double sentTaskPercent = (sentTasksCount / TASK_COUNT) * 100;
        System.out.println("[Generator] Sent tasks: " + sentTaskPercent + "%");

        double removedTaskPercent = (removedTasks / TASK_COUNT) * 100;
        System.out.println("[Generator] Removed tasks: " + removedTaskPercent + "%");

        try {
            shutDownTheProcessor();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void shutDownTheProcessor() throws InterruptedException {
        while (!Thread.State.WAITING.equals(context.getProcessorThread().getState())) {
            System.out.println("[Generator] Processor is not ready for shut down!");
            wait(TIME_TO_SLEEP);
        }

        System.out.println("[Generator] Shut down the processor!");
        context.getProcessorThread().interrupt();
    }

    private synchronized void waitUntilProcessorIsReady() throws InterruptedException {
        while (!Thread.State.WAITING.equals(context.getProcessorThread().getState())) {
            System.out.println("[Generator] Processor is not ready!");
            wait(TIME_TO_SLEEP);
        }
        System.out.println("[Generator] Processor is ready!");
    }

    private void work() throws InterruptedException {
        Process process = generateProcess();

        if (isProcessorReady()) {
            System.out.println("[Generator] Sending process into processor!");
            synchronized (context.getProcessor()) {
                context.getProcessor().setProcess(process);
                context.getProcessor().notify();
            }
            sentTasksCount++;
        } else {
            System.out.println("[Generator] Processor is busy! Removing task...");
            removedTasks++;
        }

        Thread.sleep(TIME_TO_SLEEP);
    }

    private Process generateProcess() {
        int randMin = 1;
        int randMax = 10;
        int timeToCompleteProcess = randMin + (int) (Math.random() * randMax);
        Process process = new Process(timeToCompleteProcess, processId);

        String messagePattern = "[Generator] Generating Process with id '%d' and time to complete '%d' seconds.";
        String message = String.format(messagePattern, processId, timeToCompleteProcess);
        System.out.println(message);

        processId++;
        return process;
    }

    private boolean isProcessorReady() {
        return !context.getProcessor().getIsWorking().get();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
