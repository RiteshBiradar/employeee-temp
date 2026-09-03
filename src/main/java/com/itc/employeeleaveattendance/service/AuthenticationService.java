package com.itc.employeeleaveattendance.service;

import com.itc.employeeleaveattendance.model.Employee;

import java.util.List;

/**
 * Service interface for authentication operations.
 */
public interface AuthenticationService {

    /**
     * Authenticates an employee by their ID (seeded-employee login).
     * Returns the Employee if found, throws AuthenticationException if not.
     */
    Employee login(int empId);

    /**
     * Returns all employees for the login dropdown.
     */
    List<Employee> getAllEmployees();
}
