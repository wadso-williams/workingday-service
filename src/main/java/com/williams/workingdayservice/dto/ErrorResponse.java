package com.williams.workingdayservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object to represent error response messages for the next working service controllers.
 *
 * @author williams.adeho
 */
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private int code;

    private List<String> errors = new ArrayList<>();

    public ErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ErrorResponse() {
    }

    /**
     * Enables errors to be added to the list of exciting errors
     *
     * @param error the exception error to be added
     */
    public void addError(String error) {
        this.errors.add(error);
    }

    /**
     * Gets a list of all the errors added
     *
     * @return a list of errors added
     */
    public List<String> getErrors() {
        return this.errors;
    }

    /**
     * Gets the message of the error
     *
     * @return the message of the error
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message of the error
     *
     * @param message the message of the error
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the status code of the error
     *
     * @return the status code of the error
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Sets the status code of the error
     *
     * @param code the status code of the error
     */
    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "ErrorResponse [message=" + this.message + ", code=" + this.code + ", errors=" + this.errors + "]";
    }
}