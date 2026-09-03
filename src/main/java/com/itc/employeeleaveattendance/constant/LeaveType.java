package com.itc.employeeleaveattendance.constant;

/**
 * Leave type constants.
 */
public final class LeaveType {

    public static final String CASUAL = "CASUAL";
    public static final String SICK = "SICK";
    public static final String EARNED = "EARNED";

    private LeaveType() {
    }

    /**
     * Checks if the given type is a valid leave type.
     */
    public static boolean isValid(String type) {
        if (type == null) return false;
        return type.equals(CASUAL) || type.equals(SICK) || type.equals(EARNED);
    }
}
