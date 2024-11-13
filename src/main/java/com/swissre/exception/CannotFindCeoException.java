package com.swissre.exception;

public class CannotFindCeoException extends RuntimeException {
    public CannotFindCeoException() {
        super("Unable to find CEO! At least one employee should have an empty 'managerId' field.");
    }
}
