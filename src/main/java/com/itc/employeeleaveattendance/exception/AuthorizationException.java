package com.itc.employeeleaveattendance.exception;

/**
 * Thrown when a user attempts an unauthorized action.
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
