package campus_nexus.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for Campus Nexus.
 * This class intercept all exceptions thrown by controllers and formats them
 * into a professional, consistent JSON structure.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Adding Logger to track errors in the server console for debugging
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles business logic exceptions like booking conflicts.
     * Maps to 409 Conflict.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        // Logical separation based on message content
        if (ex.getMessage().contains("Invalid") || ex.getMessage().contains("not exist") || ex.getMessage().contains("not found")) {
            status = HttpStatus.BAD_REQUEST;
        }

        // Log the warning for monitoring
        logger.warn("Business Logic Exception at {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, status);
    }

    /**
     * Handles data validation errors (e.g., empty fields in JSON).
     * Maps to 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        logger.error("Validation failed for request at {}: {}", request.getRequestURI(), fieldErrors);

        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", fieldErrors);
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all handler for any unexpected system errors.
     * Prevents internal server details from leaking to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {

        // Critical log for unexpected crashes
        logger.error("Unexpected Error at {}: ", request.getRequestURI(), ex);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "A system error occurred. Please contact the administrator.",
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}