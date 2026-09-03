package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.exception.InvalidLeaveRequestException;
import com.itc.employeeleaveattendance.exception.LeaveBalanceException;
import com.itc.employeeleaveattendance.exception.ValidationException;
import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.service.LeaveBalanceService;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
import com.itc.employeeleaveattendance.service.impl.LeaveBalanceServiceImpl;
import com.itc.employeeleaveattendance.service.impl.LeaveRequestServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles leave application.
 * GET  → display the apply leave form.
 * POST → validate and submit leave request, redirect to leave history.
 */
public class ApplyLeaveServlet extends HttpServlet {

    private final LeaveRequestService leaveRequestService;
    private final LeaveBalanceService leaveBalanceService;

    public ApplyLeaveServlet() {
        this.leaveRequestService = new LeaveRequestServiceImpl();
        this.leaveBalanceService = new LeaveBalanceServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int empId = (int) session.getAttribute("empId");

        // Load balance for display on the form
        LeaveBalance balance = leaveBalanceService.getBalance(empId);
        request.setAttribute("balance", balance);

        request.getRequestDispatcher("/WEB-INF/views/employee/apply-leave.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int empId = (int) session.getAttribute("empId"); // From session, NEVER from request param

        String leaveType = request.getParameter("leaveType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String reason = request.getParameter("reason");

        try {
            // Parse dates
            LocalDate startDate = parseDate(startDateStr, "Start date");
            LocalDate endDate = parseDate(endDateStr, "End date");

            // Delegate to service (all validation happens there)
            leaveRequestService.applyLeave(empId, leaveType, startDate, endDate, reason);

            // PRG: redirect to leave history with success message
            session.setAttribute("successMessage", "Leave application submitted successfully.");
            response.sendRedirect(request.getContextPath() + "/employee/leave-history");

        } catch (ValidationException | InvalidLeaveRequestException | LeaveBalanceException e) {
            // Re-display form with error
            request.setAttribute("error", e.getMessage());
            request.setAttribute("leaveType", leaveType);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.setAttribute("reason", reason);

            LeaveBalance balance = leaveBalanceService.getBalance(empId);
            request.setAttribute("balance", balance);

            request.getRequestDispatcher("/WEB-INF/views/employee/apply-leave.jsp").forward(request, response);

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid date format. Please use the date picker.");
            LeaveBalance balance = leaveBalanceService.getBalance(empId);
            request.setAttribute("balance", balance);
            request.getRequestDispatcher("/WEB-INF/views/employee/apply-leave.jsp").forward(request, response);
        }
    }

    private LocalDate parseDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
        return LocalDate.parse(dateStr);
    }
}
