package com.itc.employeeleaveattendance.exception;

/**
 * Thrown when input validation fails.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
