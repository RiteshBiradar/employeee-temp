package com.itc.employeeleaveattendance.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateUtil.calculateWorkingDays().
 */
class DateUtilTest {

    @Test
    @DisplayName("Monday to Friday = 5 working days")
    void testMondayToFriday() {
        // 2026-09-07 is Monday, 2026-09-11 is Friday
        LocalDate monday = LocalDate.of(2026, 9, 7);
        LocalDate friday = LocalDate.of(2026, 9, 11);
        assertEquals(5, DateUtil.calculateWorkingDays(monday, friday));
    }

    @Test
    @DisplayName("Friday to Monday = 2 working days")
    void testFridayToMonday() {
        // 2026-09-04 is Friday, 2026-09-07 is Monday
        LocalDate friday = LocalDate.of(2026, 9, 4);
        LocalDate monday = LocalDate.of(2026, 9, 7);
        assertEquals(2, DateUtil.calculateWorkingDays(friday, monday));
    }

    @Test
    @DisplayName("Saturday to Sunday = 0 working days")
    void testSaturdayToSunday() {
        // 2026-09-05 is Saturday, 2026-09-06 is Sunday
        LocalDate saturday = LocalDate.of(2026, 9, 5);
        LocalDate sunday = LocalDate.of(2026, 9, 6);
        assertEquals(0, DateUtil.calculateWorkingDays(saturday, sunday));
    }

    @Test
    @DisplayName("Single weekday = 1 working day")
    void testSingleWeekday() {
        LocalDate wednesday = LocalDate.of(2026, 9, 9);
        assertEquals(1, DateUtil.calculateWorkingDays(wednesday, wednesday));
    }

    @Test
    @DisplayName("Single weekend day = 0 working days")
    void testSingleWeekendDay() {
        LocalDate saturday = LocalDate.of(2026, 9, 5);
        assertEquals(0, DateUtil.calculateWorkingDays(saturday, saturday));
    }

    @Test
    @DisplayName("Two full weeks = 10 working days")
    void testTwoWeeks() {
        LocalDate start = LocalDate.of(2026, 9, 7); // Monday
        LocalDate end = LocalDate.of(2026, 9, 18);   // Friday
        assertEquals(10, DateUtil.calculateWorkingDays(start, end));
    }

    @Test
    @DisplayName("Cross-week span: Wednesday to Tuesday = 5 working days")
    void testCrossWeek() {
        // Wed Sep 9 to Tue Sep 15 = Wed,Thu,Fri,Mon,Tue = 5
        LocalDate start = LocalDate.of(2026, 9, 9);
        LocalDate end = LocalDate.of(2026, 9, 15);
        assertEquals(5, DateUtil.calculateWorkingDays(start, end));
    }

    @Test
    @DisplayName("Start date after end date throws exception")
    void testStartAfterEnd() {
        LocalDate start = LocalDate.of(2026, 9, 10);
        LocalDate end = LocalDate.of(2026, 9, 8);
        assertThrows(IllegalArgumentException.class, () ->
                DateUtil.calculateWorkingDays(start, end));
    }

    @Test
    @DisplayName("Null start date throws exception")
    void testNullStartDate() {
        assertThrows(IllegalArgumentException.class, () ->
                DateUtil.calculateWorkingDays(null, LocalDate.of(2026, 9, 10)));
    }

    @Test
    @DisplayName("Null end date throws exception")
    void testNullEndDate() {
        assertThrows(IllegalArgumentException.class, () ->
                DateUtil.calculateWorkingDays(LocalDate.of(2026, 9, 10), null));
    }

    @Test
    @DisplayName("Only Saturday = 0 working days")
    void testOnlySaturday() {
        LocalDate sat = LocalDate.of(2026, 9, 12);
        assertEquals(0, DateUtil.calculateWorkingDays(sat, sat));
    }

    @Test
    @DisplayName("Only Sunday = 0 working days")
    void testOnlySunday() {
        LocalDate sun = LocalDate.of(2026, 9, 13);
        assertEquals(0, DateUtil.calculateWorkingDays(sun, sun));
    }

    @Test
    @DisplayName("One month range has correct working days")
    void testOneMonth() {
        // September 2026: starts on Tuesday
        // Weekdays: 1-4 (4), 7-11 (5), 14-18 (5), 21-25 (5), 28-30 (3) = 22
        LocalDate start = LocalDate.of(2026, 9, 1);
        LocalDate end = LocalDate.of(2026, 9, 30);
        assertEquals(22, DateUtil.calculateWorkingDays(start, end));
    }
}
