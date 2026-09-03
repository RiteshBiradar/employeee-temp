package com.itc.employeeleaveattendance.service;

import com.itc.employeeleaveattendance.dto.PendingLeaveRequestDTO;
import com.itc.employeeleaveattendance.model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for leave request operations.
 */
public interface LeaveRequestService {

    /**
     * Submits a new leave application for an employee.
     * Performs all validations (dates, balance, overlap).
     */
    void applyLeave(int empId, String leaveType, LocalDate startDate, LocalDate endDate, String reason);

    /**
     * Returns all leave requests for a given employee.
     */
    List<LeaveRequest> getEmployeeLeaveRequests(int empId);

    /**
     * Returns pending leave requests for direct reports of the given manager.
     */
    List<PendingLeaveRequestDTO> getPendingRequestsForManager(int managerId);

    /**
     * Approves a pending leave request. Transactional operation.
     * Validates manager authorization, request status, and leave balance.
     */
    void approveLeave(int requestId, int managerId);

    /**
     * Rejects a pending leave request.
     * Validates manager authorization and request status.
     */
    void rejectLeave(int requestId, int managerId);
}
