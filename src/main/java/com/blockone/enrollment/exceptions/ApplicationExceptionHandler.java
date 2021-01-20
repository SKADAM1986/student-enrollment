package com.blockone.enrollment.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.blockone.enrollment.models.ErrorResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    /**
     * CreditLimitExceededException is handled and appropriate response is sent to client
     * @Param ex
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(CreditLimitExceededException.class)
    protected ResponseEntity<Object> handleCreditLimitExceededException(
            CreditLimitExceededException ex) {
        log.error("ApplicationExceptionHandler.handleCreditLimitExceededException");
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage()),HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * DataNotFoundException is handled and appropriate response is sent to client
     * @Param ex
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<Object> handleDataNotFoundException(
            DataNotFoundException ex) {
        log.error("ApplicationExceptionHandler.handleDataNotFoundException");
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()),HttpStatus.NOT_FOUND);
    }


    /**
     *  All types of Exceptions will be handled and appropriate response is sent to client
     * @Param ex
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(
            Exception ex) {
        log.error("ApplicationExceptionHandler.handleException");
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
