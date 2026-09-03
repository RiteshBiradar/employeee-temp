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
    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }
}
