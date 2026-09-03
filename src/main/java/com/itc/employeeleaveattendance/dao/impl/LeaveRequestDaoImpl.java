package com.itc.employeeleaveattendance.dao.impl;

import com.itc.employeeleaveattendance.dao.LeaveRequestDao;
import com.itc.employeeleaveattendance.dto.PendingLeaveRequestDTO;
import com.itc.employeeleaveattendance.model.LeaveRequest;
import com.itc.employeeleaveattendance.util.DBUtil;
import com.itc.employeeleaveattendance.util.DateUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of LeaveRequestDao.
 */
public class LeaveRequestDaoImpl implements LeaveRequestDao {

    private static final Logger LOGGER = Logger.getLogger(LeaveRequestDaoImpl.class.getName());

    @Override
    public void create(LeaveRequest request) {
        String sql = "INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason) " +
                     "VALUES (leave_requests_seq.NEXTVAL, ?, ?, ?, ?, 'PENDING', ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, request.getEmpId());
            ps.setString(2, request.getLeaveType());
            ps.setDate(3, Date.valueOf(request.getStartDate()));
            ps.setDate(4, Date.valueOf(request.getEndDate()));
            ps.setString(5, request.getReason());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating leave request", e);
            throw new RuntimeException("Database error while creating leave request.", e);
        }
    }

    @Override
    public LeaveRequest findById(int requestId) {
        String sql = "SELECT request_id, emp_id, leave_type, start_date, end_date, status, reason " +
                     "FROM leave_requests WHERE request_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding leave request by ID: " + requestId, e);
            throw new RuntimeException("Database error while finding leave request.", e);
        }
        return null;
    }

    @Override
    public LeaveRequest findById(int requestId, Connection conn) {
        String sql = "SELECT request_id, emp_id, leave_type, start_date, end_date, status, reason " +
                     "FROM leave_requests WHERE request_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding leave request by ID (transactional): " + requestId, e);
            throw new RuntimeException("Database error while finding leave request.", e);
        }
        return null;
    }

    @Override
    public List<LeaveRequest> findByEmpId(int empId) {
        String sql = "SELECT request_id, emp_id, leave_type, start_date, end_date, status, reason " +
                     "FROM leave_requests WHERE emp_id = ? ORDER BY start_date DESC";
        List<LeaveRequest> requests = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding leave requests for employee: " + empId, e);
            throw new RuntimeException("Database error while fetching leave requests.", e);
        }
        return requests;
    }

    @Override
    public List<PendingLeaveRequestDTO> findPendingByManagerId(int managerId) {
        String sql = "SELECT lr.request_id, lr.emp_id, e.name AS employee_name, " +
                     "lr.leave_type, lr.start_date, lr.end_date, lr.reason, lr.status " +
                     "FROM leave_requests lr " +
                     "JOIN employees e ON lr.emp_id = e.emp_id " +
                     "WHERE e.manager_id = ? AND lr.status = 'PENDING' " +
                     "ORDER BY lr.start_date ASC";
        List<PendingLeaveRequestDTO> dtos = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PendingLeaveRequestDTO dto = new PendingLeaveRequestDTO();
                    dto.setRequestId(rs.getInt("request_id"));
                    dto.setEmpId(rs.getInt("emp_id"));
                    dto.setEmployeeName(rs.getString("employee_name"));
                    dto.setLeaveType(rs.getString("leave_type"));
                    LocalDate start = rs.getDate("start_date").toLocalDate();
                    LocalDate end = rs.getDate("end_date").toLocalDate();
                    dto.setStartDate(start);
                    dto.setEndDate(end);
                    dto.setWorkingDays(DateUtil.calculateWorkingDays(start, end));
                    dto.setReason(rs.getString("reason"));
                    dto.setStatus(rs.getString("status"));
                    dtos.add(dto);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding pending requests for manager: " + managerId, e);
            throw new RuntimeException("Database error while fetching pending leave requests.", e);
        }
        return dtos;
    }

    @Override
    public void updateStatus(int requestId, String status, Connection conn) {
        String sql = "UPDATE leave_requests SET status = ? WHERE request_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Leave request not found: " + requestId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating leave request status: " + requestId, e);
            throw new RuntimeException("Database error while updating leave request status.", e);
        }
    }

    @Override
    public boolean hasOverlappingLeave(int empId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM leave_requests " +
                     "WHERE emp_id = ? AND status IN ('PENDING', 'APPROVED') " +
                     "AND start_date <= ? AND end_date >= ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking overlapping leave for employee: " + empId, e);
            throw new RuntimeException("Database error while checking overlapping leave.", e);
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a LeaveRequest object.
     */
    private LeaveRequest mapRow(ResultSet rs) throws SQLException {
        LeaveRequest req = new LeaveRequest();
        req.setRequestId(rs.getInt("request_id"));
        req.setEmpId(rs.getInt("emp_id"));
        req.setLeaveType(rs.getString("leave_type"));
        req.setStartDate(rs.getDate("start_date").toLocalDate());
        req.setEndDate(rs.getDate("end_date").toLocalDate());
        req.setStatus(rs.getString("status"));
        req.setReason(rs.getString("reason"));
        return req;
    }
}
