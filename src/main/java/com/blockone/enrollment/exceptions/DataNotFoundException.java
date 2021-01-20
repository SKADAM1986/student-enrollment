package com.blockone.enrollment.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataNotFoundException extends RuntimeException {
    private final Logger log = LoggerFactory.getLogger(DataNotFoundException.class);

    public DataNotFoundException(String message) {
        super(message);
        log.error("DataNotFoundException CONSTRUCTOR called");
    }

    public DataNotFoundException() {
        super("Data Not Found for requested resource");
        log.error("DataNotFoundException CONSTRUCTOR called");
    }
}
