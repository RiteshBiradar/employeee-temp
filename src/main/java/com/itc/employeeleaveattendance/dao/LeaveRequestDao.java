package com.itc.employeeleaveattendance.dao;

import com.itc.employeeleaveattendance.dto.PendingLeaveRequestDTO;
import com.itc.employeeleaveattendance.model.LeaveRequest;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access interface for LeaveRequest operations.
 */
public interface LeaveRequestDao {

    /**
     * Creates a new leave request.
     */
    void create(LeaveRequest request);

    /**
     * Finds a leave request by its ID.
     */
    LeaveRequest findById(int requestId);

    /**
     * Finds and locks a leave request by its ID using the given connection (for transactions).
     */
    LeaveRequest findById(int requestId, Connection conn);

    /**
     * Finds all leave requests for a given employee, ordered by start_date descending.
     */
    List<LeaveRequest> findByEmpId(int empId);

    /**
     * Finds pending leave requests for employees whose manager_id matches the given managerId.
     * Returns DTOs with employee name included.
     */
    List<PendingLeaveRequestDTO> findPendingByManagerId(int managerId);

    /**
     * Updates the status of a leave request using the provided connection (for transactions).
     */
    void updateStatus(int requestId, String status, Connection conn);

    /**
     * Checks whether the employee has any overlapping pending or approved leave requests
     * for the given date range.
     */
    boolean hasOverlappingLeave(int empId, LocalDate startDate, LocalDate endDate);
}
