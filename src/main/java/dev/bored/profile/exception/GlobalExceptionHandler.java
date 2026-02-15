package dev.bored.profile.exception;

import dev.bored.common.exception.ErrorResponse;
import dev.bored.common.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Global exception handler that keeps error responses simple and consistent.
 * <p>
 * Intercepts exceptions thrown by controllers and translates them into
 * structured JSON error responses with appropriate HTTP status codes.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link GenericException} instances thrown from the service layer.
     * <p>
     * Logs the error at WARN level and returns a JSON body containing the
     * status code, reason phrase, and detail message.
     * </p>
     *
     * @param ex the {@link GenericException} to handle
     * @return a {@link ResponseEntity} with a structured error map and the
     *         appropriate HTTP status
     */
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(GenericException ex) {
        log.warn("Error [{}]: {}", ex.getStatus().value(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.of(ex, null));
    }

    /**
     * Catch-all handler for unexpected {@link RuntimeException} instances.
     * <p>
     * Logs the full stack trace at ERROR level and returns a generic
     * 500 Internal Server Error response to avoid leaking implementation details.
     * </p>
     *
     * @param ex the {@link RuntimeException} to handle
     * @return a {@link ResponseEntity} with a generic 500 error map
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(500, "Internal Server Error", "An unexpected error occurred", null));
    }
}
