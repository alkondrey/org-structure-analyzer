package com.swissre.exception;

import static java.lang.String.format;

public class InvalidFileHeaderException extends RuntimeException {
    public InvalidFileHeaderException(String actualHeader, String expectedHeader) {
        super(format("Invalid file header. Actual: %s Expected: %s", actualHeader, expectedHeader));
    }
}
