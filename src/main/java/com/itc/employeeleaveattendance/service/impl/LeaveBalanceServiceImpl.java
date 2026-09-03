package com.itc.employeeleaveattendance.service.impl;

import com.itc.employeeleaveattendance.dao.LeaveBalanceDao;
import com.itc.employeeleaveattendance.dao.impl.LeaveBalanceDaoImpl;
import com.itc.employeeleaveattendance.model.LeaveBalance;
import com.itc.employeeleaveattendance.service.LeaveBalanceService;

/**
 * Implementation of LeaveBalanceService.
 */
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceDao leaveBalanceDao;

    public LeaveBalanceServiceImpl() {
        this.leaveBalanceDao = new LeaveBalanceDaoImpl();
    }

    // Constructor for testing
    public LeaveBalanceServiceImpl(LeaveBalanceDao leaveBalanceDao) {
        this.leaveBalanceDao = leaveBalanceDao;
    }

    @Override
    public LeaveBalance getBalance(int empId) {
        return leaveBalanceDao.findByEmpId(empId);
    }
}
