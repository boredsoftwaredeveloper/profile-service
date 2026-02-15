package dev.bored.profile.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenericException}.
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
class GenericExceptionTest {

    @Test
    void constructor_WithMessageAndStatus_ShouldSetBoth() {
        GenericException ex = new GenericException("Not found", HttpStatus.NOT_FOUND);

        assertEquals("Not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void constructor_WithMessageOnly_ShouldDefaultToInternalServerError() {
        GenericException ex = new GenericException("Something went wrong");

        assertEquals("Something went wrong", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void shouldBeRuntimeException() {
        GenericException ex = new GenericException("error", HttpStatus.BAD_REQUEST);

        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void getStatus_ShouldReturnAssignedStatus() {
        GenericException ex = new GenericException("forbidden", HttpStatus.FORBIDDEN);

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
        assertEquals(403, ex.getStatus().value());
    }
}
