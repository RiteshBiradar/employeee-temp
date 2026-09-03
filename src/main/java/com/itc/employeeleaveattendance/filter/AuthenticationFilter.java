package com.itc.employeeleaveattendance.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authentication filter that checks whether a user is logged in.
 * Protects all URLs under /employee/* and /manager/*.
 * If no valid session exists, redirects to the login page.
 */
public class AuthenticationFilter implements Filter {

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

        if (session == null || session.getAttribute("empId") == null) {
            // Not authenticated — redirect to login
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
            return;
        }

        // User is authenticated, continue
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
