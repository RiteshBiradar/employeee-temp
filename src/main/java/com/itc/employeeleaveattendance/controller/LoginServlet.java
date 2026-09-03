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

        // Load all employees for dropdown
        List<Employee> employees = authService.getAllEmployees();
        request.setAttribute("employees", employees);

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String empIdStr = request.getParameter("empId");

        if (empIdStr == null || empIdStr.isBlank()) {
            request.setAttribute("error", "Please select an employee account.");
            List<Employee> employees = authService.getAllEmployees();
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            int empId = Integer.parseInt(empIdStr);
            Employee employee = authService.login(empId);

            // Create session and store employee info (role from DB, not from client)
            HttpSession session = request.getSession(true);
            session.setAttribute("empId", employee.getEmpId());
            session.setAttribute("empName", employee.getName());
            session.setAttribute("role", employee.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Redirect to appropriate dashboard based on role
            if (Role.MANAGER.equals(employee.getRole())) {
                response.sendRedirect(request.getContextPath() + "/manager/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/employee/dashboard");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid selection.");
            List<Employee> employees = authService.getAllEmployees();
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } catch (AuthenticationException e) {
            request.setAttribute("error", e.getMessage());
            List<Employee> employees = authService.getAllEmployees();
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
