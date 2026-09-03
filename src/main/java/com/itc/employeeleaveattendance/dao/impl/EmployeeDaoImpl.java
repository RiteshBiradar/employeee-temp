package com.itc.employeeleaveattendance.dao.impl;

import com.itc.employeeleaveattendance.dao.EmployeeDao;
import com.itc.employeeleaveattendance.model.Employee;
import com.itc.employeeleaveattendance.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of EmployeeDao.
 */
public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDaoImpl.class.getName());

    @Override
    public Employee findById(int empId) {
        String sql = "SELECT emp_id, name, email, role, manager_id FROM employees WHERE emp_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding employee by ID: " + empId, e);
            throw new RuntimeException("Database error while finding employee.", e);
        }
        return null;
    }

    @Override
    public Employee findByEmail(String email) {
        String sql = "SELECT emp_id, name, email, role, manager_id FROM employees WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding employee by email: " + email, e);
            throw new RuntimeException("Database error while finding employee.", e);
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT emp_id, name, email, role, manager_id FROM employees ORDER BY role, name";
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                employees.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all employees", e);
            throw new RuntimeException("Database error while fetching employees.", e);
        }
        return employees;
    }

    /**
     * Maps a ResultSet row to an Employee object.
     */
    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmpId(rs.getInt("emp_id"));
        emp.setName(rs.getString("name"));
        emp.setEmail(rs.getString("email"));
        emp.setRole(rs.getString("role"));
        int managerId = rs.getInt("manager_id");
        emp.setManagerId(rs.wasNull() ? null : managerId);
        return emp;
    }
}
