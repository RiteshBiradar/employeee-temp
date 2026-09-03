package com.itc.employeeleaveattendance.service;

import com.itc.employeeleaveattendance.model.LeaveBalance;

/**
 * Service interface for leave balance operations.
 */
public interface LeaveBalanceService {

    /**
     * Returns the leave balance for a given employee.
     */
    LeaveBalance getBalance(int empId);
}
