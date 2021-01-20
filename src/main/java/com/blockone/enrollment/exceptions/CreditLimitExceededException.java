package com.blockone.enrollment.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreditLimitExceededException extends RuntimeException{
    private final Logger log = LoggerFactory.getLogger(CreditLimitExceededException.class);

    public CreditLimitExceededException(String message) {
        super(message);
        log.error("ApplicationExceptionHandler.CreditLimitExceededException() CONSTRUCTOR called");
    }

}
