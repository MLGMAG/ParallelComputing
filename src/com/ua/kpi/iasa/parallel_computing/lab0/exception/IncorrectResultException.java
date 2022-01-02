package com.ua.kpi.iasa.parallel_computing.lab0.exception;

public class IncorrectResultException extends RuntimeException {
    public IncorrectResultException() {
        super("Incorrect result!");
    }
}
