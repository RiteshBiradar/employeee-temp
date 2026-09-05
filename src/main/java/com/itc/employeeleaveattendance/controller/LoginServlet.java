package com.itc.employeeleaveattendance.controller;

import com.itc.employeeleaveattendance.constant.Role;
import com.itc.employeeleaveattendance.exception.AuthenticationException;
import com.itc.employeeleaveattendance.model.Employee;
import com.itc.employeeleaveattendance.service.AuthenticationService;
import com.itc.employeeleaveattendance.service.impl.AuthenticationServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Handles login functionality.
 * GET  → display login page with seeded employee dropdown.
 * POST → authenticate selected employee, create session, redirect to dashboard.
 */
public class LoginServlet extends HttpServlet {

    private final AuthenticationService authService;

    public LoginServlet() {
        this.authService = new AuthenticationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If already logged in, redirect to appropriate dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("empId") != null) {
            String role = (String) session.getAttribute("role");
            if (Role.MANAGER.equals(role)) {
                response.sendRedirect(request.getContextPath() + "/manager/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/employee/dashboard");
            }
            return;
        }

        // Show email/password login form
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Please enter email and password.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            Employee employee = authService.login(email.trim(), password);

            HttpSession session = request.getSession(true);
            session.setAttribute("empId", employee.getEmpId());
            session.setAttribute("empName", employee.getName());
            session.setAttribute("role", employee.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            if (Role.MANAGER.equals(employee.getRole())) {
                response.sendRedirect(request.getContextPath() + "/manager/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/employee/dashboard");
            }

        } catch (AuthenticationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
