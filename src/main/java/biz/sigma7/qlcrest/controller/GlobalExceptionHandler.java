package biz.sigma7.qlcrest.controller;

import biz.sigma7.qlcrest.domain.exception.DownstreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DownstreamException.class)
    public ResponseEntity<String> handleDownstreamException(DownstreamException ex) {
        LOG.error("Downstream exception", ex);
        return ResponseEntity
                .status(BAD_GATEWAY)
                .body(ex.getMessage());
    }
}
