package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.model.LeaveRequest;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
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
 * Displays the employee's leave request history.
 */
public class LeaveHistoryServlet extends HttpServlet {

    private final LeaveRequestService leaveRequestService;

    public LeaveHistoryServlet() {
        this.leaveRequestService = new LeaveRequestServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int empId = (int) session.getAttribute("empId");

        List<LeaveRequest> leaveRequests = leaveRequestService.getEmployeeLeaveRequests(empId);
        request.setAttribute("leaveRequests", leaveRequests);

        // Pass DateUtil wrapper for working days calculation in JSP
        request.setAttribute("dateUtil", new EmployeeDashboardServlet.DateUtilWrapper());

        // Check for success message from PRG redirect
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        request.getRequestDispatcher("/WEB-INF/views/employee/leave-history.jsp").forward(request, response);
    }
}
