-- ============================================================
-- Employee Leave & Attendance Tracker - Oracle Database Schema
-- ============================================================
-- Execute this script in Oracle SQL*Plus, SQL Developer, or
-- any Oracle-compatible SQL client.
-- ============================================================

-- =========================
-- DROP EXISTING OBJECTS
-- =========================
-- Run these only if re-creating the schema from scratch.
-- Comment out if running for the first time.

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE leave_balances CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE leave_requests CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE employees CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE employees_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE leave_requests_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE leave_balances_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- =========================
-- SEQUENCES
-- =========================

CREATE SEQUENCE employees_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE SEQUENCE leave_requests_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE SEQUENCE leave_balances_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- =========================
-- EMPLOYEES TABLE
-- =========================

CREATE TABLE employees (
    emp_id      NUMBER(10)    DEFAULT employees_seq.NEXTVAL PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL,
    email       VARCHAR2(100) NOT NULL UNIQUE,
    role        VARCHAR2(20)  NOT NULL CHECK (role IN ('EMPLOYEE', 'MANAGER')),
    password    VARCHAR2(255),
    manager_id  NUMBER(10),
    CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES employees(emp_id)
);

-- =========================
-- LEAVE REQUESTS TABLE
-- =========================

CREATE TABLE leave_requests (
    request_id  NUMBER(10)    DEFAULT leave_requests_seq.NEXTVAL PRIMARY KEY,
    emp_id      NUMBER(10)    NOT NULL,
    leave_type  VARCHAR2(20)  NOT NULL CHECK (leave_type IN ('CASUAL', 'SICK', 'EARNED')),
    start_date  DATE          NOT NULL,
    end_date    DATE          NOT NULL,
    status      VARCHAR2(20)  DEFAULT 'PENDING' NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    reason      VARCHAR2(1000) NOT NULL,
    CONSTRAINT fk_leave_employee FOREIGN KEY (emp_id) REFERENCES employees(emp_id),
    CONSTRAINT chk_dates CHECK (end_date >= start_date)
);

CREATE INDEX idx_leave_emp_id ON leave_requests(emp_id);
CREATE INDEX idx_leave_status ON leave_requests(status);

-- =========================
-- LEAVE BALANCES TABLE
-- =========================

CREATE TABLE leave_balances (
    balance_id      NUMBER(10)  DEFAULT leave_balances_seq.NEXTVAL PRIMARY KEY,
    emp_id          NUMBER(10)  NOT NULL UNIQUE,
    casual_balance  NUMBER(5,1) DEFAULT 12 NOT NULL,
    sick_balance    NUMBER(5,1) DEFAULT 12 NOT NULL,
    earned_balance  NUMBER(5,1) DEFAULT 15 NOT NULL,
    CONSTRAINT fk_balance_employee FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);

-- =========================
-- SAMPLE DATA
-- =========================

-- Insert Managers first (no manager_id for top-level managers)
INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Priya Sharma', 'priya.sharma@company.com', 'MANAGER', NULL);

INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Rajesh Kumar', 'rajesh.kumar@company.com', 'MANAGER', NULL);

-- Insert Employees under Manager 1 (Priya Sharma, emp_id = 1)
INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Anita Desai', 'anita.desai@company.com', 'EMPLOYEE', 1);

INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Vikram Patel', 'vikram.patel@company.com', 'EMPLOYEE', 1);

INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Sneha Iyer', 'sneha.iyer@company.com', 'EMPLOYEE', 1);

-- Insert Employees under Manager 2 (Rajesh Kumar, emp_id = 2)
INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Amit Verma', 'amit.verma@company.com', 'EMPLOYEE', 2);

INSERT INTO employees (emp_id, name, email, role, manager_id)
    VALUES (employees_seq.NEXTVAL, 'Kavita Nair', 'kavita.nair@company.com', 'EMPLOYEE', 2);

-- Leave Balances for all employees
INSERT INTO leave_balances (balance_id, emp_id, casual_balance, sick_balance, earned_balance)
    VALUES (leave_balances_seq.NEXTVAL, 3, 12, 12, 15);

INSERT INTO leave_balances (balance_id, emp_id, casual_balance, sick_balance, earned_balance)
    VALUES (leave_balances_seq.NEXTVAL, 4, 12, 12, 15);

INSERT INTO leave_balances (balance_id, emp_id, casual_balance, sick_balance, earned_balance)
    VALUES (leave_balances_seq.NEXTVAL, 5, 12, 12, 15);

INSERT INTO leave_balances (balance_id, emp_id, casual_balance, sick_balance, earned_balance)
    VALUES (leave_balances_seq.NEXTVAL, 6, 12, 12, 15);

INSERT INTO leave_balances (balance_id, emp_id, casual_balance, sick_balance, earned_balance)
    VALUES (leave_balances_seq.NEXTVAL, 7, 12, 12, 15);

-- Sample Leave Requests for demonstration
-- Anita Desai (emp_id=3): Approved casual leave
INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason)
    VALUES (leave_requests_seq.NEXTVAL, 3, 'CASUAL', TO_DATE('2026-08-18', 'YYYY-MM-DD'), TO_DATE('2026-08-20', 'YYYY-MM-DD'), 'APPROVED', 'Family function');

-- Update balance for approved leave (3 working days: Mon-Wed)
UPDATE leave_balances SET casual_balance = 9 WHERE emp_id = 3;

-- Vikram Patel (emp_id=4): Pending sick leave
INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason)
    VALUES (leave_requests_seq.NEXTVAL, 4, 'SICK', TO_DATE('2026-09-08', 'YYYY-MM-DD'), TO_DATE('2026-09-10', 'YYYY-MM-DD'), 'PENDING', 'Medical appointment and recovery');

-- Sneha Iyer (emp_id=5): Rejected earned leave
INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason)
    VALUES (leave_requests_seq.NEXTVAL, 5, 'EARNED', TO_DATE('2026-08-25', 'YYYY-MM-DD'), TO_DATE('2026-08-29', 'YYYY-MM-DD'), 'REJECTED', 'Planned vacation');

-- Amit Verma (emp_id=6): Pending casual leave
INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason)
    VALUES (leave_requests_seq.NEXTVAL, 6, 'CASUAL', TO_DATE('2026-09-15', 'YYYY-MM-DD'), TO_DATE('2026-09-17', 'YYYY-MM-DD'), 'PENDING', 'Personal work');

-- Kavita Nair (emp_id=7): Pending sick leave
INSERT INTO leave_requests (request_id, emp_id, leave_type, start_date, end_date, status, reason)
    VALUES (leave_requests_seq.NEXTVAL, 7, 'SICK', TO_DATE('2026-09-10', 'YYYY-MM-DD'), TO_DATE('2026-09-12', 'YYYY-MM-DD'), 'PENDING', 'Fever and cold');

COMMIT;

-- Set demo plaintext passwords for seeded employees (CHANGE TO HASHED IN PRODUCTION)
UPDATE employees SET password = 'manager123' WHERE email IN ('priya.sharma@company.com','rajesh.kumar@company.com');
UPDATE employees SET password = 'password123' WHERE email IN ('anita.desai@company.com','vikram.patel@company.com','sneha.iyer@company.com','amit.verma@company.com','kavita.nair@company.com');

COMMIT;

-- ============================================================
-- END OF SCHEMA
-- ============================================================
