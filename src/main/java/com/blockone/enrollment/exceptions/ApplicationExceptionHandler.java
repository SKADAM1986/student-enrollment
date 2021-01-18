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

    @ExceptionHandler(CreditLimitExceededException.class)
    protected ResponseEntity<Object> handleCreditLimitExceededException(
            CreditLimitExceededException ex) {
        log.error("ApplicationExceptionHandler.handleCreditLimitExceededException() START");
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "1000"),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(
            Exception ex) {
        log.error("ApplicationExceptionHandler.handleException() START");
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "500"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
