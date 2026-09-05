package com.itc.employeeleaveattendance.service.impl;

import com.itc.employeeleaveattendance.dao.EmployeeDao;
import com.itc.employeeleaveattendance.dao.impl.EmployeeDaoImpl;
import com.itc.employeeleaveattendance.exception.AuthenticationException;
import com.itc.employeeleaveattendance.model.Employee;
import com.itc.employeeleaveattendance.service.AuthenticationService;

import java.util.List;

/**
 * Implementation of AuthenticationService.
 * Uses seeded-employee selection for demo authentication.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    private final EmployeeDao employeeDao;

    public AuthenticationServiceImpl() {
        this.employeeDao = new EmployeeDaoImpl();
    }

    // Constructor for testing
    public AuthenticationServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee login(int empId) {
        Employee employee = employeeDao.findById(empId);
        if (employee == null) {
            throw new AuthenticationException("Employee not found. Please select a valid account.");
        }
        return employee;
    }

    @Override
    public Employee login(String email, String password) {
        Employee employee = employeeDao.findByEmail(email);
        if (employee == null) {
            throw new AuthenticationException("Invalid credentials.");
        }

        String stored = employee.getPassword();
        if (stored == null) {
            throw new AuthenticationException("Invalid credentials.");
        }

        // Use bcrypt to verify password
        try {
            boolean ok = org.mindrot.jbcrypt.BCrypt.checkpw(password, stored);
            if (!ok) {
                throw new AuthenticationException("Invalid credentials.");
            }
        } catch (IllegalArgumentException e) {
            // Stored value may be plaintext (legacy) — fall back to plaintext compare
            if (!stored.equals(password)) {
                throw new AuthenticationException("Invalid credentials.");
            }
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }
}
