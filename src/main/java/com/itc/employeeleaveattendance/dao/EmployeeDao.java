package com.itc.employeeleaveattendance.dao;

import com.itc.employeeleaveattendance.model.Employee;

import java.util.List;

/**
 * Data access interface for Employee operations.
 */
public interface EmployeeDao {

    /**
     * Find an employee by their ID.
     */
    Employee findById(int empId);

    /**
     * Find an employee by their email address.
     */
    Employee findByEmail(String email);

    /**
     * Find all employees (used for login dropdown).
     */
    List<Employee> findAll();
}
