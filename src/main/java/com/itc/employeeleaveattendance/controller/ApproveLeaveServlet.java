package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.exception.AuthorizationException;
import com.itc.employeeleaveattendance.exception.InvalidLeaveRequestException;
import com.itc.employeeleaveattendance.exception.LeaveBalanceException;
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
 * Handles leave request approval by managers.
 * POST only — PRG redirect back to manager dashboard.
 */
public class ApproveLeaveServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApproveLeaveServlet.class.getName());

    private final LeaveRequestService leaveRequestService;

    public ApproveLeaveServlet() {
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

            // Service handles all validation and transactional approval
            leaveRequestService.approveLeave(requestId, managerId);

            session.setAttribute("successMessage", "Leave request approved successfully.");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID.");
        } catch (AuthorizationException | InvalidLeaveRequestException | LeaveBalanceException e) {
            session.setAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Error approving leave request", e);
            session.setAttribute("errorMessage", "An unexpected error occurred while approving the request.");
        }

        // PRG: redirect back to manager dashboard
        response.sendRedirect(request.getContextPath() + "/manager/dashboard");
    }
}
