package dev.bored.profile.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Simple generic exception for all service-layer errors.
 * <p>
 * Carries an {@link HttpStatus} so the {@link dev.bored.profile.exception.GlobalExceptionHandler}
 * can map it directly to an appropriate HTTP response.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
public class GenericException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    /**
     * Constructs a new {@code GenericException} with the specified detail message
     * and HTTP status.
     *
     * @param message the detail message describing the error
     * @param status  the {@link HttpStatus} to associate with this exception
     */
    public GenericException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Constructs a new {@code GenericException} with the specified detail message
     * and a default status of {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     *
     * @param message the detail message describing the error
     */
    public GenericException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Returns the HTTP status associated with this exception.
     *
     * @return the {@link HttpStatus} for this exception
     */
    public HttpStatus getStatus() {
        return status;
    }
}
