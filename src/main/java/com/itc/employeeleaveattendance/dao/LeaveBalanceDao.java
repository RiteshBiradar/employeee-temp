package com.itc.employeeleaveattendance.dao;

import com.itc.employeeleaveattendance.model.LeaveBalance;

import java.sql.Connection;

/**
 * Data access interface for LeaveBalance operations.
 */
public interface LeaveBalanceDao {

    /**
     * Find leave balance for an employee.
     */
    LeaveBalance findByEmpId(int empId);

    /**
     * Find leave balance for an employee using the given connection (for transactions).
     */
    LeaveBalance findByEmpId(int empId, Connection conn);

    /**
     * Deduct leave balance for a specific leave type.
     * Uses the provided connection for transactional operations.
     *
     * @param empId     the employee ID
     * @param leaveType the leave type (CASUAL, SICK, EARNED)
     * @param days      the number of working days to deduct
     * @param conn      the database connection (transaction)
     */
    void deductBalance(int empId, String leaveType, int days, Connection conn);
}
