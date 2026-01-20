package dev.bored.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the Profile Service.
 * <p>
 * This class provides centralized exception handling across all controllers,
 * ensuring consistent error responses throughout the application.
 * </p>
 *
 * @author Bored Software Developer
 * @version 1.0
 * @since 2026-01-21
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all RuntimeExceptions thrown by controllers.
     * <p>
     * Returns HTTP 500 Internal Server Error with the exception message.
     * </p>
     *
     * @param ex the RuntimeException that was thrown
     * @return ResponseEntity with error message and HTTP 500 status
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
