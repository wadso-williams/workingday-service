package com.williams.workingdayservice.exception;

/**
 * API exception for all Runtime exceptions across the Next Working Day service.
 *
 * @author williams.adeho
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an API Exception with the specified message
     *
     * @param message the exception's message
     */
    public ApiException(String message) {
        super(message);
    }

    /**
     * Creates an API Exception with the specified message and cause of the exception
     *
     * @param message the exception's message
     * @param cause the cause of the exception
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
