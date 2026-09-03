package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.dto.PendingLeaveRequestDTO;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
import com.itc.employeeleaveattendance.service.impl.LeaveRequestServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Manager dashboard. Displays pending leave requests from direct reports.
 */
public class ManagerDashboardServlet extends HttpServlet {

    private final LeaveRequestService leaveRequestService;

    public ManagerDashboardServlet() {
        this.leaveRequestService = new LeaveRequestServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int managerId = (int) session.getAttribute("empId");

        // Get pending leave requests for direct reports only
        List<PendingLeaveRequestDTO> pendingRequests =
                leaveRequestService.getPendingRequestsForManager(managerId);
        request.setAttribute("pendingRequests", pendingRequests);

        // Check for success/error messages from PRG redirect
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        request.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(request, response);
    }
}
