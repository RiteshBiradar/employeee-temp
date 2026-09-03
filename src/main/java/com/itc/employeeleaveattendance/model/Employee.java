package com.itc.employeeleaveattendance.model;

import java.io.Serializable;

/**
 * Represents an employee in the system.
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private int empId;
    private String name;
    private String email;
    private String role;
    private Integer managerId;

    public Employee() {
    }

    public Employee(int empId, String name, String email, String role, Integer managerId) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.managerId = managerId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{empId=" + empId + ", name='" + name + "', role='" + role + "'}";
    }
}
