package com.itc.employeeleaveattendance.model;

import java.io.Serializable;

/**
 * Represents leave balance for an employee.
 */
public class LeaveBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    private int balanceId;
    private int empId;
    private double casualBalance;
    private double sickBalance;
    private double earnedBalance;

    public LeaveBalance() {
    }

    public LeaveBalance(int balanceId, int empId, double casualBalance,
                        double sickBalance, double earnedBalance) {
        this.balanceId = balanceId;
        this.empId = empId;
        this.casualBalance = casualBalance;
        this.sickBalance = sickBalance;
        this.earnedBalance = earnedBalance;
    }

    public int getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public double getCasualBalance() {
        return casualBalance;
    }

    public void setCasualBalance(double casualBalance) {
        this.casualBalance = casualBalance;
    }

    public double getSickBalance() {
        return sickBalance;
    }

    public void setSickBalance(double sickBalance) {
        this.sickBalance = sickBalance;
    }

    public double getEarnedBalance() {
        return earnedBalance;
    }

    public void setEarnedBalance(double earnedBalance) {
        this.earnedBalance = earnedBalance;
    }

    /**
     * Returns the balance for the given leave type.
     */
    public double getBalanceForType(String leaveType) {
        return switch (leaveType.toUpperCase()) {
            case "CASUAL" -> casualBalance;
            case "SICK" -> sickBalance;
            case "EARNED" -> earnedBalance;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return "LeaveBalance{empId=" + empId +
               ", casual=" + casualBalance +
               ", sick=" + sickBalance +
               ", earned=" + earnedBalance + "}";
    }
}
