# Project Name: SmartOps - Enterprise Workflow & Analytics Platform

## 1. Project Overview
**Description:** SmartOps is an internal enterprise platform designed to digitize business workflows (e.g., leave requests, equipment procurement) and provide an interactive dashboard for performance analytics. 
**Objective:** Create a portfolio project tailored for a Management Services company, demonstrating strong skills in Business Process Understanding, RESTful APIs, Data Visualization, and Automation.

## 2. Tech Stack Requirements
### Frontend [React JS]
- **Core:** React JS (Functional Components, Hooks)
- **Styling:** Tailwind CSS + Material-UI (Enterprise UI/UX look)
- **State Management:** Zustand or Redux Toolkit
- **Data Fetching:** Axios
- **Data Visualization:** Recharts (for Dashboards)
- **Routing:** React Router DOM

### Backend [Java Spring Boot]
- **Core:** Java 21, Spring Boot 3.x, Spring Web
- **Data Access:** Spring Data JPA, Hibernate
- **Security:** Spring Security + JWT (JSON Web Token) for Role-Based Access Control (RBAC)
- **Database:** MySQL
- **API Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Testing:** JUnit 5 + Mockito

### Tools & Integrations
- **Version Control:** Git (implement Git Flow)
- **Automation:** JavaMailSender (Email notifications) or RestTemplate/WebClient (Webhook to Telegram/Slack)
- **Database Migration (Optional but recommended):** Flyway or Liquibase

## 3. Database Schema (Updated)

### Entity Relationships
```
Department (1) ──< User (N)
Role      (1) ──< User (N)
User      (1) ──< Ticket (N)  [as created_by]
User      (1) ──< Ticket (N)  [as assigned_to]
Ticket    (1) ──< NotificationLog (N)
```

### Table Definitions

#### `department`
| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT PK | Auto-increment |
| `name` | VARCHAR(100) | UNIQUE, NOT NULL |
| `description` | VARCHAR(255) | Optional |
| `manager_id` | BIGINT FK → user | Nullable (set after users created) |
| `created_at` | DATETIME | Auto-set |

---

#### `role`
| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT PK | Auto-increment |
| `role_name` | ENUM | `ADMIN`, `MANAGER`, `EMPLOYEE` |

---

#### `user`
| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT PK | Auto-increment |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL |
| `password` | VARCHAR(255) | BCrypt hashed |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL |
| `full_name` | VARCHAR(100) | NOT NULL |
| `department_id` | BIGINT FK → department | NOT NULL |
| `role_id` | BIGINT FK → role | NOT NULL |
| `is_active` | BOOLEAN | Default: TRUE |
| `created_at` | DATETIME | Auto-set |

---

#### `ticket`
| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT PK | Auto-increment |
| `title` | VARCHAR(255) | NOT NULL |
| `description` | TEXT | NOT NULL |
| `ticket_type` | ENUM | `LEAVE`, `PROCUREMENT`, `OTHER` |
| `status` | ENUM | `PENDING`, `APPROVED`, `REJECTED` |
| `priority` | ENUM | `LOW`, `MEDIUM`, `HIGH` |
| `created_by` | BIGINT FK → user | Employee who created |
| `assigned_to` | BIGINT FK → user | Manager who handles |
| `rejection_reason` | VARCHAR(500) | Nullable — filled when REJECTED |
| `attachment_url` | VARCHAR(500) | Nullable — link to uploaded file |
| `created_at` | DATETIME | Auto-set |
| `updated_at` | DATETIME | Auto-updated |
| `resolved_at` | DATETIME | Nullable — set when APPROVED/REJECTED |

---

#### `notification_log`
| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT PK | Auto-increment |
| `ticket_id` | BIGINT FK → ticket | NOT NULL |
| `message` | TEXT | NOT NULL |
| `channel` | ENUM | `EMAIL`, `TELEGRAM`, `SLACK` |
| `sent_at` | DATETIME | Auto-set |
| `status` | ENUM | `SUCCESS`, `FAILED` |

---

## 4. Core Modules & Features for Implementation

### Module 1: Authentication & Authorization
- **Backend:** Implement login/register endpoints. Generate JWT token on successful login. Role-based endpoint protection.
- **Frontend:** Login page, storing JWT in local storage/cookies, Protected Routes based on user role.

### Module 2: Dynamic Workflow & Ticket Management (Business Logic)
- **Employee Role:** Can create new tickets (e.g., "Request Leave", "Request Laptop"), view their own ticket history and statuses.
- **Manager Role:** Can view all pending tickets assigned to their department, approve or reject tickets (with `rejection_reason`).
- **API Requirements:** CRUD operations for Tickets. Status transition logic (`PENDING` → `APPROVED` / `REJECTED`). Set `resolved_at` timestamp on status change.

### Module 3: Interactive Dashboard & Reporting (Analytics)
- **Backend:** Create aggregation APIs to return statistics:
  - Total tickets / count by `status`
  - Tickets by `department`
  - Tickets by `ticket_type`
  - **Average Resolution Time** (AVG of `resolved_at - created_at`)
- **Frontend:** Build a Dashboard view using `Recharts`:
  - Pie chart: ticket status distribution
  - Bar chart: tickets by department
  - KPI card: Avg Resolution Time (in hours)

### Module 4: Automation & Integrations
- **Backend Event Listener:** Whenever a ticket `status` changes to `APPROVED` or `REJECTED`, trigger an event.
- **Action:** Send automated Email to ticket creator OR trigger a Webhook (HTTP POST) to a simulated Slack/Telegram endpoint.
- **Log** the result to `notification_log` with `channel` and `status`.

### Module 5: Testing & Documentation
- Ensure Swagger UI is exposed at `/swagger-ui.html`.
- Write Unit Tests for the `TicketService` business logic using JUnit 5 and Mockito.

## 5. Instructions for AI Agent
Please act as an Expert Full-Stack Developer. Execute the project in the following phases:
1. **Phase 1: Backend Setup:** Initialize Spring Boot, connect to MySQL, define JPA Entities (Department, Role, User, Ticket, NotificationLog), and build the basic Auth/JWT module.
2. **Phase 2: Core API:** Develop the Ticket Management APIs and the Aggregation APIs for the Dashboard (including Avg Resolution Time). Include Swagger.
3. **Phase 3: Automation & Testing:** Add the Webhook/Email notification logic (log to `notification_log` with channel), and write unit tests for core services.
4. **Phase 4: Frontend Setup:** Initialize React app, setup Tailwind, and build the Login + Layout components.
5. **Phase 5: Frontend Integration:** Connect Frontend to Backend APIs. Build the Ticket CRUD UI and the Recharts Dashboard (Pie, Bar charts + KPI cards).

*Note for AI: Prioritize clean code, SOLID principles, proper exception handling (GlobalExceptionHandler in Spring Boot), and responsive design in React.*
