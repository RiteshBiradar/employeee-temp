package com.itc.employeeleaveattendance.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a leave request submitted by an employee.
 */
public class LeaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private int requestId;
    private int empId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String reason;

    public LeaveRequest() {
    }

    public LeaveRequest(int requestId, int empId, String leaveType,
                        LocalDate startDate, LocalDate endDate, String status, String reason) {
        this.requestId = requestId;
        this.empId = empId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LeaveRequest{requestId=" + requestId + ", empId=" + empId +
               ", leaveType='" + leaveType + "', status='" + status + "'}";
    }
}
