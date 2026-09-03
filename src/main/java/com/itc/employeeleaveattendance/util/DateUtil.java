package com.itc.employeeleaveattendance.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Utility class for date and working-day calculations.
 */
public final class DateUtil {

    private DateUtil() {
    }

    /**
     * Calculates the number of working days between two dates (inclusive).
     * Working days are Monday through Friday.
     * Saturday and Sunday are excluded.
     *
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return the number of working days, or 0 if the range is invalid or contains only weekends
     * @throws IllegalArgumentException if startDate is after endDate
     */
    public static int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        int workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            DayOfWeek day = current.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }
}
