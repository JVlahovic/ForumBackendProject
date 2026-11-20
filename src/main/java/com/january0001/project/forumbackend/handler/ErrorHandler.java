package com.january0001.project.forumbackend.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<String> handleResponseStatus(ResponseStatusException rse) {
        log.error("Request failed with status {}: {}", rse.getStatusCode(), rse.getReason());
        String reason = rse.getReason() != null
                ? rse.getReason()
                : rse.getStatusCode().toString();
        return new ResponseEntity<>(reason, rse.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed.");
        log.error("Validation failed: {}", message);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> handleUnreadable(HttpMessageNotReadableException ex) {
        log.error("Malformed or missing request body: {}", ex.getMessage());
        return new ResponseEntity<>("Malformed or missing request body.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        String message = ex.getMessage() != null ? ex.getMessage() : "Resource not found.";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(
                "This operation conflicts with existing data.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return new ResponseEntity<>(
                "Something went wrong. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
