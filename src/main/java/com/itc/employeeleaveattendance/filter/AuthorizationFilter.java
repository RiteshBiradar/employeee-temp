package com.itc.employeeleaveattendance.filter;

import com.itc.employeeleaveattendance.constant.Role;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authorization filter that enforces role-based access control.
 *
 * URL mapping:
 *   /employee/* → requires EMPLOYEE role
 *   /manager/*  → requires MANAGER role
 *
 * If the user's role does not match, they are forwarded to the access-denied page.
 */
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession(false);

        // Session should already exist (AuthenticationFilter runs first)
        if (session == null || session.getAttribute("role") == null) {
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        String requestURI = httpReq.getRequestURI();
        String contextPath = httpReq.getContextPath();

        // Determine required role based on URL
        if (requestURI.startsWith(contextPath + "/manager")) {
            // Manager URLs require MANAGER role
            if (!Role.MANAGER.equals(role)) {
                httpResp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpReq.getRequestDispatcher("/WEB-INF/views/common/access-denied.jsp")
                       .forward(httpReq, httpResp);
                return;
            }
        } else if (requestURI.startsWith(contextPath + "/employee")) {
            // Employee URLs require EMPLOYEE role
            if (!Role.EMPLOYEE.equals(role)) {
                httpResp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpReq.getRequestDispatcher("/WEB-INF/views/common/access-denied.jsp")
                       .forward(httpReq, httpResp);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
