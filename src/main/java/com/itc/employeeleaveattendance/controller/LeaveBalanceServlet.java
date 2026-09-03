package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.service.LeaveBalanceService;
import com.itc.employeeleaveattendance.service.impl.LeaveBalanceServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Displays the employee's leave balance.
 */
public class LeaveBalanceServlet extends HttpServlet {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceServlet() {
        this.leaveBalanceService = new LeaveBalanceServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        int empId = (int) session.getAttribute("empId");

        LeaveBalance balance = leaveBalanceService.getBalance(empId);
        request.setAttribute("balance", balance);

        request.getRequestDispatcher("/WEB-INF/views/employee/leave-balance.jsp").forward(request, response);
    }
}
