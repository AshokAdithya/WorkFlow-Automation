package com.pixels.zapierClone.automation_platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle RuntimeExceptions thrown anywhere in your app
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // You can customize status and message here
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Optionally handle other specific exceptions
    // @ExceptionHandler(SomeSpecificException.class)
    // public ResponseEntity<String> handleSpecificException(SomeSpecificException ex) {
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    // }
}