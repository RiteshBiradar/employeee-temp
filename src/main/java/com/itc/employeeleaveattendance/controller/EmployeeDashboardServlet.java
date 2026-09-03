package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.model.LeaveRequest;
import com.itc.employeeleaveattendance.service.LeaveBalanceService;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
import com.itc.employeeleaveattendance.service.impl.LeaveBalanceServiceImpl;
import com.itc.employeeleaveattendance.service.impl.LeaveRequestServiceImpl;
import com.itc.employeeleaveattendance.util.DateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Employee dashboard. Displays leave balance summary and recent leave requests.
 */
public class EmployeeDashboardServlet extends HttpServlet {

    private final LeaveBalanceService leaveBalanceService;
    private final LeaveRequestService leaveRequestService;

    public EmployeeDashboardServlet() {
        this.leaveBalanceService = new LeaveBalanceServiceImpl();
        this.leaveRequestService = new LeaveRequestServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int empId = (int) session.getAttribute("empId");

        // Load leave balance
        LeaveBalance balance = leaveBalanceService.getBalance(empId);
        request.setAttribute("balance", balance);

        // Load recent leave requests
        List<LeaveRequest> leaveRequests = leaveRequestService.getEmployeeLeaveRequests(empId);
        request.setAttribute("leaveRequests", leaveRequests);

        // Add working days calculator utility for display
        request.setAttribute("dateUtil", new DateUtilWrapper());

        request.getRequestDispatcher("/WEB-INF/views/employee/dashboard.jsp").forward(request, response);
    }

    /**
     * Wrapper to expose DateUtil.calculateWorkingDays in EL.
     */
    public static class DateUtilWrapper {
        public int calculateWorkingDays(java.time.LocalDate start, java.time.LocalDate end) {
            return DateUtil.calculateWorkingDays(start, end);
        }
    }
}
