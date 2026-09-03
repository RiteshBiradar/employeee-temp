package com.itc.employeeleaveattendance.service.impl;

import com.itc.employeeleaveattendance.constant.LeaveStatus;
import com.itc.employeeleaveattendance.constant.LeaveType;
import com.itc.employeeleaveattendance.dao.EmployeeDao;
import com.itc.employeeleaveattendance.dao.LeaveBalanceDao;
import com.itc.employeeleaveattendance.dao.LeaveRequestDao;
import com.itc.employeeleaveattendance.dao.impl.EmployeeDaoImpl;
import com.itc.employeeleaveattendance.dao.impl.LeaveBalanceDaoImpl;
import com.itc.employeeleaveattendance.dao.impl.LeaveRequestDaoImpl;
import com.itc.employeeleaveattendance.dto.PendingLeaveRequestDTO;
import com.itc.employeeleaveattendance.exception.*;
import com.itc.employeeleaveattendance.model.Employee;
import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.model.LeaveRequest;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
import com.itc.employeeleaveattendance.util.DBUtil;
import com.itc.employeeleaveattendance.util.DateUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of LeaveRequestService.
 * Contains all business logic for leave management.
 */
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private static final Logger LOGGER = Logger.getLogger(LeaveRequestServiceImpl.class.getName());

    private final LeaveRequestDao leaveRequestDao;
    private final LeaveBalanceDao leaveBalanceDao;
    private final EmployeeDao employeeDao;

    public LeaveRequestServiceImpl() {
        this.leaveRequestDao = new LeaveRequestDaoImpl();
        this.leaveBalanceDao = new LeaveBalanceDaoImpl();
        this.employeeDao = new EmployeeDaoImpl();
    }

    // Constructor for testing
    public LeaveRequestServiceImpl(LeaveRequestDao leaveRequestDao,
                                    LeaveBalanceDao leaveBalanceDao,
                                    EmployeeDao employeeDao) {
        this.leaveRequestDao = leaveRequestDao;
        this.leaveBalanceDao = leaveBalanceDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public void applyLeave(int empId, String leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        // Validate inputs
        if (leaveType == null || leaveType.isBlank()) {
            throw new ValidationException("Leave type is required.");
        }
        if (!LeaveType.isValid(leaveType)) {
            throw new ValidationException("Invalid leave type: " + leaveType);
        }
        if (startDate == null) {
            throw new ValidationException("Start date is required.");
        }
        if (endDate == null) {
            throw new ValidationException("End date is required.");
        }
        if (reason == null || reason.isBlank()) {
            throw new ValidationException("Reason is required.");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        // Calculate working days
        int workingDays = DateUtil.calculateWorkingDays(startDate, endDate);
        if (workingDays == 0) {
            throw new ValidationException("The selected date range contains no working days (weekdays only).");
        }

        // Check for overlapping leave
        if (leaveRequestDao.hasOverlappingLeave(empId, startDate, endDate)) {
            throw new InvalidLeaveRequestException(
                    "You already have a pending or approved leave request that overlaps with the selected dates.");
        }

        // Check leave balance
        LeaveBalance balance = leaveBalanceDao.findByEmpId(empId);
        if (balance == null) {
            throw new LeaveBalanceException("Leave balance record not found. Please contact your administrator.");
        }
        double available = balance.getBalanceForType(leaveType);
        if (available < workingDays) {
            throw new LeaveBalanceException(
                    "Insufficient " + leaveType.toLowerCase() + " leave balance. " +
                    "Available: " + (int) available + " days, Requested: " + workingDays + " days.");
        }

        // Create leave request (status defaults to PENDING; balance NOT deducted yet)
        LeaveRequest request = new LeaveRequest();
        request.setEmpId(empId);
        request.setLeaveType(leaveType);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setReason(reason);

        leaveRequestDao.create(request);
    }

    @Override
    public List<LeaveRequest> getEmployeeLeaveRequests(int empId) {
        return leaveRequestDao.findByEmpId(empId);
    }

    @Override
    public List<PendingLeaveRequestDTO> getPendingRequestsForManager(int managerId) {
        return leaveRequestDao.findPendingByManagerId(managerId);
    }

    @Override
    public void approveLeave(int requestId, int managerId) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. Fetch the leave request
            LeaveRequest request = leaveRequestDao.findById(requestId, conn);
            if (request == null) {
                throw new InvalidLeaveRequestException("Leave request not found.");
            }

            // 2. Verify request is PENDING
            if (!LeaveStatus.PENDING.equals(request.getStatus())) {
                throw new InvalidLeaveRequestException(
                        "Cannot approve a request that is already " + request.getStatus() + ".");
            }

            // 3. Verify the manager is the employee's direct manager
            Employee employee = employeeDao.findById(request.getEmpId());
            if (employee == null) {
                throw new InvalidLeaveRequestException("Employee not found for this leave request.");
            }
            if (employee.getManagerId() == null || employee.getManagerId() != managerId) {
                throw new AuthorizationException(
                        "You are not authorized to approve this leave request. The employee does not report to you.");
            }

            // 4. Calculate working days
            int workingDays = DateUtil.calculateWorkingDays(request.getStartDate(), request.getEndDate());

            // 5. Verify sufficient leave balance
            LeaveBalance balance = leaveBalanceDao.findByEmpId(request.getEmpId(), conn);
            if (balance == null) {
                throw new LeaveBalanceException("Leave balance record not found for employee.");
            }
            double available = balance.getBalanceForType(request.getLeaveType());
            if (available < workingDays) {
                throw new LeaveBalanceException(
                        "Insufficient leave balance to approve. Available: " + (int) available +
                        " days, Required: " + workingDays + " days.");
            }

            // 6. Update leave request status to APPROVED
            leaveRequestDao.updateStatus(requestId, LeaveStatus.APPROVED, conn);

            // 7. Deduct leave balance
            leaveBalanceDao.deductBalance(request.getEmpId(), request.getLeaveType(), workingDays, conn);

            // 8. COMMIT
            conn.commit();
            LOGGER.info("Leave request " + requestId + " approved by manager " + managerId);

        } catch (SQLException e) {
            rollback(conn);
            LOGGER.log(Level.SEVERE, "Database error during leave approval", e);
            throw new RuntimeException("Database error during leave approval.", e);
        } catch (RuntimeException e) {
            rollback(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void rejectLeave(int requestId, int managerId) {
        // Fetch the leave request
        LeaveRequest request = leaveRequestDao.findById(requestId);
        if (request == null) {
            throw new InvalidLeaveRequestException("Leave request not found.");
        }

        // Verify request is PENDING
        if (!LeaveStatus.PENDING.equals(request.getStatus())) {
            throw new InvalidLeaveRequestException(
                    "Cannot reject a request that is already " + request.getStatus() + ".");
        }

        // Verify the manager is the employee's direct manager
        Employee employee = employeeDao.findById(request.getEmpId());
        if (employee == null) {
            throw new InvalidLeaveRequestException("Employee not found for this leave request.");
        }
        if (employee.getManagerId() == null || employee.getManagerId() != managerId) {
            throw new AuthorizationException(
                    "You are not authorized to reject this leave request. The employee does not report to you.");
        }

        // Update status to REJECTED — no balance deduction
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            leaveRequestDao.updateStatus(requestId, LeaveStatus.REJECTED, conn);
            LOGGER.info("Leave request " + requestId + " rejected by manager " + managerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during leave rejection", e);
            throw new RuntimeException("Database error during leave rejection.", e);
        } finally {
            closeConnection(conn);
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error during rollback", ex);
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error closing connection", ex);
            }
        }
    }
}
