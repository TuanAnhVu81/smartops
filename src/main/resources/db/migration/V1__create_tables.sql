-- =====================================================================
-- V1__create_tables.sql
-- SmartOps - Initial Schema Migration
-- Tables are created in FK-dependency order (parent tables first)
-- =====================================================================

-- ── 1. department ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS department
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),

    -- manager_id is nullable (set after users are created via V2 seed)
    manager_id  BIGINT       NULL,

    -- Audit fields (managed by Spring Data JPA Auditing / BaseEntity)
    created_by  VARCHAR(100) NULL,
    created_at  DATETIME(6)  NULL,
    updated_by  VARCHAR(100) NULL,
    updated_at  DATETIME(6)  NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 2. role ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS role
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- VARCHAR instead of ENUM for flexibility and Spring Security compatibility
    role_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 3. user ────────────────────────────────────────────────────────
-- `user` is a reserved keyword in some SQL dialects, using backticks is safe practice
CREATE TABLE IF NOT EXISTS `user`
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    full_name     VARCHAR(100) NOT NULL,

    -- Nullable: System Admin may not belong to a department
    department_id BIGINT       NULL,
    role_id       BIGINT       NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,

    -- Audit fields (managed by Spring Data JPA Auditing / BaseEntity)
    created_by    VARCHAR(100) NULL,
    created_at    DATETIME(6)  NULL,
    updated_by    VARCHAR(100) NULL,
    updated_at    DATETIME(6)  NULL,

    CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE SET NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Now that `user` table exists, add the manager FK on department
ALTER TABLE department
    ADD CONSTRAINT fk_department_manager FOREIGN KEY (manager_id) REFERENCES `user` (id) ON DELETE SET NULL;

-- ── 4. ticket ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ticket
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      TEXT         NOT NULL,
    ticket_type      VARCHAR(20)  NOT NULL,  -- LEAVE, PROCUREMENT, OTHER
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED
    priority         VARCHAR(10)  NOT NULL DEFAULT 'MEDIUM',   -- LOW, MEDIUM, HIGH

    -- created_by_user FK is separate from BaseEntity's created_by (string username)
    created_by_user  BIGINT       NOT NULL,
    assigned_to      BIGINT       NULL,

    rejection_reason VARCHAR(500) NULL,
    attachment_url   VARCHAR(500) NULL,

    -- Soft delete: records are not physically removed but excluded from most queries
    is_deleted       BOOLEAN      NOT NULL DEFAULT FALSE,

    -- Set when ticket is APPROVED or REJECTED — used for KPI calculation
    resolved_at      DATETIME(6)  NULL,

    -- Audit fields (managed by Spring Data JPA Auditing / BaseEntity)
    created_by       VARCHAR(100) NULL,
    created_at       DATETIME(6)  NULL,
    updated_by       VARCHAR(100) NULL,
    updated_at       DATETIME(6)  NULL,

    CONSTRAINT fk_ticket_created_by_user FOREIGN KEY (created_by_user) REFERENCES `user` (id),
    CONSTRAINT fk_ticket_assigned_to FOREIGN KEY (assigned_to) REFERENCES `user` (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 5. ticket_history ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS ticket_history
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id       BIGINT       NOT NULL,
    actor_id        BIGINT       NOT NULL,  -- The user who performed the action

    -- Action type: CREATED, STATUS_CHANGED, REASSIGNED, etc.
    action          VARCHAR(50)  NOT NULL,
    previous_status VARCHAR(50)  NULL,
    current_status  VARCHAR(50)  NOT NULL,
    comment         VARCHAR(500) NULL,

    -- Only created_at needed (audit log is immutable, never updated)
    -- DATETIME(6) prevents sort collision if multiple events happen in the same second
    created_at      DATETIME(6)  NULL,

    CONSTRAINT fk_ticket_history_ticket FOREIGN KEY (ticket_id) REFERENCES ticket (id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_history_actor FOREIGN KEY (actor_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 6. notification_log ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notification_log
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT      NOT NULL,
    message   TEXT        NOT NULL,
    channel   VARCHAR(20) NOT NULL,  -- EMAIL, TELEGRAM, SLACK
    status    VARCHAR(10) NOT NULL,  -- SUCCESS, FAILED
    sent_at   DATETIME(6) NULL,

    CONSTRAINT fk_notification_ticket FOREIGN KEY (ticket_id) REFERENCES ticket (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── Indexes for Dashboard Analytics performance ────────────────────

-- Analytics: Count tickets by type or status
CREATE INDEX idx_ticket_type           ON ticket (ticket_type, is_deleted);

-- Dashboard: "Assigned To Me" + Filter by Status (Composite Index)
-- Perfect for query: WHERE assigned_to = ? AND is_deleted = false AND status = ?
CREATE INDEX idx_ticket_assigned_status ON ticket (assigned_to, is_deleted, status);

-- Dashboard: "Created By Me"
CREATE INDEX idx_ticket_created_by     ON ticket (created_by_user, is_deleted);

-- KPI: Avg Resolution Time (Filtering resolved tickets over time)
CREATE INDEX idx_ticket_status_resolved ON ticket (status, resolved_at);

-- Performance: Fast lookup for ticket history rendering
CREATE INDEX idx_ticket_history_ticket ON ticket_history (ticket_id, created_at);

-- Index on notification_log for fast lookup by ticket
CREATE INDEX idx_notification_ticket   ON notification_log (ticket_id);
