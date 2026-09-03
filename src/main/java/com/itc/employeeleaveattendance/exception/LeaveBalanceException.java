package com.itc.employeeleaveattendance.exception;

/**
 * Thrown when leave balance is insufficient.
 */
public class LeaveBalanceException extends RuntimeException {

    public LeaveBalanceException(String message) {
        super(message);
    }
}
