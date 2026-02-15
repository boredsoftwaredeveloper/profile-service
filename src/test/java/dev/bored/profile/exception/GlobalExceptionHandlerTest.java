package dev.bored.profile.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleGeneric_ShouldReturnCorrectStatusAndBody() {
        GenericException ex = new GenericException("Profile not found", HttpStatus.NOT_FOUND);

        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("Profile not found", body.get("message"));
    }

    @Test
    void handleGeneric_WithBadRequest_ShouldReturn400() {
        GenericException ex = new GenericException("Invalid input", HttpStatus.BAD_REQUEST);

        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
    }

    @Test
    void handleGeneric_WithInternalServerError_ShouldReturn500() {
        GenericException ex = new GenericException("Unexpected failure");

        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().get("status"));
    }

    @Test
    void handleRuntime_ShouldReturn500WithGenericMessage() {
        RuntimeException ex = new RuntimeException("NullPointerException somewhere");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
    }

    @Test
    void handleRuntime_ShouldNotLeakExceptionDetails() {
        RuntimeException ex = new RuntimeException("secret DB password leaked");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertNotEquals("secret DB password leaked", response.getBody().get("message"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
    }
}
