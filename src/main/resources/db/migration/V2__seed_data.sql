-- =====================================================================
-- V2__seed_data.sql
-- SmartOps - Initial Seed Data for Development & Demo
-- =====================================================================

-- ── 1. Seed Roles (aligned with Spring Security convention) ────────
INSERT INTO role (role_name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_MANAGER'),
    ('ROLE_EMPLOYEE');

-- ── 2. Seed Departments ────────────────────────────────────────────
INSERT INTO department (name, description) VALUES
    ('Engineering', 'Software development and infrastructure team'),
    ('Human Resources', 'HR operations and recruitment'),
    ('Finance', 'Finance, accounting and budgeting'),
    ('Operations', 'Business operations and management services');

-- ── 3. Seed Users ─────────────────────────────────────────────────
-- All passwords are BCrypt hash of "Password@123"
-- Generated with BCryptPasswordEncoder.encode("Password@123")
INSERT INTO `user` (username, email, full_name, password, department_id, role_id, is_active) VALUES
    -- Admin (no department required)
    ('admin', 'admin@smartops.com', 'System Administrator', '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', NULL, 1, TRUE),

    -- Managers
    ('manager.eng', 'manager.eng@smartops.com', 'Nguyen Van A', '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', 1, 2, TRUE),
    ('manager.hr',  'manager.hr@smartops.com',  'Tran Thi B',   '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', 2, 2, TRUE),

    -- Employees
    ('emp.eng1',    'emp.eng1@smartops.com',    'Le Van C',     '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', 1, 3, TRUE),
    ('emp.eng2',    'emp.eng2@smartops.com',    'Pham Thi D',   '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', 1, 3, TRUE),
    ('emp.hr1',     'emp.hr1@smartops.com',     'Hoang Van E',  '$2a$10$ryIE6/gVdnb/z8XN.7JQMOZM/j1rKyXMoXrSfE.T2JB6bFQPjJXW', 2, 3, TRUE);

-- ── 4. Assign Managers to Departments ──────────────────────────────
UPDATE department SET manager_id = (SELECT id FROM `user` WHERE username = 'manager.eng') WHERE name = 'Engineering';
UPDATE department SET manager_id = (SELECT id FROM `user` WHERE username = 'manager.hr')  WHERE name = 'Human Resources';
