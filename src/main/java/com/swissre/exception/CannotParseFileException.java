package com.swissre.exception;

import static java.lang.String.format;

public class CannotParseFileException extends RuntimeException {
    public CannotParseFileException(String filePath, Exception cause) {
        super(format("Unable to parse file %s", filePath), cause);
    }
}

