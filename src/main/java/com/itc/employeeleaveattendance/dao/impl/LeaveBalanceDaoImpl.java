package com.itc.employeeleaveattendance.dao.impl;

import com.itc.employeeleaveattendance.dao.LeaveBalanceDao;
import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.util.DBUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of LeaveBalanceDao.
 */
public class LeaveBalanceDaoImpl implements LeaveBalanceDao {

    private static final Logger LOGGER = Logger.getLogger(LeaveBalanceDaoImpl.class.getName());

    @Override
    public LeaveBalance findByEmpId(int empId) {
        String sql = "SELECT balance_id, emp_id, casual_balance, sick_balance, earned_balance " +
                     "FROM leave_balances WHERE emp_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding leave balance for employee: " + empId, e);
            throw new RuntimeException("Database error while fetching leave balance.", e);
        }
        return null;
    }

    @Override
    public LeaveBalance findByEmpId(int empId, Connection conn) {
        String sql = "SELECT balance_id, emp_id, casual_balance, sick_balance, earned_balance " +
                     "FROM leave_balances WHERE emp_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding leave balance (transactional) for employee: " + empId, e);
            throw new RuntimeException("Database error while fetching leave balance.", e);
        }
        return null;
    }

    @Override
    public void deductBalance(int empId, String leaveType, int days, Connection conn) {
        String column = getColumnForLeaveType(leaveType);
        String sql = "UPDATE leave_balances SET " + column + " = " + column + " - ? WHERE emp_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            ps.setInt(2, empId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Leave balance record not found for employee: " + empId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deducting leave balance for employee: " + empId, e);
            throw new RuntimeException("Database error while deducting leave balance.", e);
        }
    }

    /**
     * Returns the database column name for the given leave type.
     * This is safe because leaveType is validated before reaching this method.
     */
    private String getColumnForLeaveType(String leaveType) {
        return switch (leaveType.toUpperCase()) {
            case "CASUAL" -> "casual_balance";
            case "SICK" -> "sick_balance";
            case "EARNED" -> "earned_balance";
            default -> throw new IllegalArgumentException("Invalid leave type: " + leaveType);
        };
    }

    private LeaveBalance mapRow(ResultSet rs) throws SQLException {
        LeaveBalance balance = new LeaveBalance();
        balance.setBalanceId(rs.getInt("balance_id"));
        balance.setEmpId(rs.getInt("emp_id"));
        balance.setCasualBalance(rs.getDouble("casual_balance"));
        balance.setSickBalance(rs.getDouble("sick_balance"));
        balance.setEarnedBalance(rs.getDouble("earned_balance"));
        return balance;
    }
}
