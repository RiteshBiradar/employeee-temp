package com.itc.employeeleaveattendance.exception;

/**
 * Thrown when a leave request is invalid (e.g., overlapping dates, invalid state transition).
 */
public class InvalidLeaveRequestException extends RuntimeException {

    public InvalidLeaveRequestException(String message) {
        super(message);
    }
}
