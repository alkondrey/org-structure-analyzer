package com.swissre.exception;

import static java.lang.String.format;

public class ExceedingFileSizeException extends RuntimeException {
    public ExceedingFileSizeException(int maxEmployeeCount) {
        super(format("The file contains more than %d employees. Please reduce the employee count.", maxEmployeeCount));
    }
}
