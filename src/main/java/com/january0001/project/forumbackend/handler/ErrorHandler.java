package com.january0001.project.forumbackend.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArg(IllegalArgumentException iae) {
        log.error("Illegal argument exception detected. Something didn't go right on the input side, check what is being passed: {}", iae.getMessage());
        return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalStateException(IllegalStateException ise) {
        log.error("Illegal state exception detected. Something broke internally, check what the data is being processed by, and what stopped it and when:{}", ise.getMessage());
        return new ResponseEntity<>(ise.getMessage(), HttpStatus.CONFLICT);
    }
}
