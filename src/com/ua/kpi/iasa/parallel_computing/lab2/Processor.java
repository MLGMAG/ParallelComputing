package com.ua.kpi.iasa.parallel_computing.lab2;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Processor implements Runnable {
    private final AtomicBoolean isWorking = new AtomicBoolean(false);

    private Context context;
    private Process process;

    @Override
    public void run() {
        while (true) {
            try {
                work();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Processor] I am interrupted. I end my execution...");
                break;
            }
        }
    }

    private void work() throws InterruptedException {
        while (Objects.isNull(process)) {
            synchronized (this) {
                wait();
            }
        }

        isWorking.set(true);
        System.out.println("[Processor] I am working!");
        String messagePattern = "[Processor] I receive process with id '%d' and time to complete '%d' seconds.";
        String message = String.format(messagePattern, process.getId(), process.getTimeToComplete());
        System.out.println(message);

        long timeToComplete = getProcess().getTimeToComplete() * 1000L;
        Thread.sleep(timeToComplete);

        setProcess(null);
        isWorking.set(false);

        System.out.println("[Processor] I go sleep!");
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }

    public AtomicBoolean getIsWorking() {
        return isWorking;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
