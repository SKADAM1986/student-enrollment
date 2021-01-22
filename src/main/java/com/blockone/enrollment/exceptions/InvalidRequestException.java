package com.blockone.enrollment.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidRequestException extends RuntimeException{
    private final transient Logger log = LoggerFactory.getLogger(InvalidRequestException.class);

    public InvalidRequestException(String message) {
        super(message);
        log.error("InvalidRequestException() CONSTRUCTOR called");
    }

}
