package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.exception.AuthorizationException;
import com.itc.employeeleaveattendance.exception.InvalidLeaveRequestException;
import com.itc.employeeleaveattendance.service.LeaveRequestService;
import com.itc.employeeleaveattendance.service.impl.LeaveRequestServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles leave request rejection by managers.
 * POST only — PRG redirect back to manager dashboard.
 */
public class RejectLeaveServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RejectLeaveServlet.class.getName());

    private final LeaveRequestService leaveRequestService;

    public RejectLeaveServlet() {
        this.leaveRequestService = new LeaveRequestServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int managerId = (int) session.getAttribute("empId"); // From session, not request

        String requestIdStr = request.getParameter("requestId");

        try {
            if (requestIdStr == null || requestIdStr.isBlank()) {
                throw new InvalidLeaveRequestException("Request ID is required.");
            }

            int requestId = Integer.parseInt(requestIdStr);

            // Service handles validation and rejection (no balance deduction)
            leaveRequestService.rejectLeave(requestId, managerId);

            session.setAttribute("successMessage", "Leave request rejected.");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID.");
        } catch (AuthorizationException | InvalidLeaveRequestException e) {
            session.setAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Error rejecting leave request", e);
            session.setAttribute("errorMessage", "An unexpected error occurred while rejecting the request.");
        }

        // PRG: redirect back to manager dashboard
        response.sendRedirect(request.getContextPath() + "/manager/dashboard");
    }
}
