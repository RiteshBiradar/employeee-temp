# Employee Leave & Attendance Tracker

**Team 5**

## Team Members

- [Member 1]
- [Member 2]
- [Member 3]

---

## Project Overview

A centralised, secure and user-friendly Java web application for managing employee leave requests with role-based access control. The system streamlines leave management, improves operational efficiency, and reduces administrative overhead.

### Key Features

- **Employee Portal**: Apply for leave (Casual/Sick/Earned), view leave history, check leave balances.
- **Manager Portal**: View pending requests from direct reports, approve or reject leave.
- **Role-Based Access Control**: Server-side enforced RBAC with separate employee and manager URL spaces.
- **Working Day Calculation**: Automatic exclusion of weekends (Saturday/Sunday).
- **Transactional Approval**: Atomic leave approval with balance deduction using database transactions.
- **Leave Balance Tracking**: Real-time balance display with deduction only on approval.

---

## Architecture

```
Browser → JSP (View) → Servlet (Controller) → Service (Business Logic) → DAO → JDBC → Oracle DB
```

### MVC Pattern

| Layer       | Responsibility                              |
|-------------|---------------------------------------------|
| **Model**   | Employee, LeaveRequest, LeaveBalance        |
| **View**    | JSP pages with JSTL/EL                     |
| **Controller** | Servlets handling HTTP requests          |
| **Service** | Business logic, validation, transactions    |
| **DAO**     | Database operations via JDBC                |
| **Filter**  | Authentication & Authorization              |

---

## Technology Stack

| Technology       | Version     |
|------------------|-------------|
| Java             | 17          |
| Jakarta Servlet  | 6.0         |
| JSP              | 3.1         |
| JSTL             | 3.0         |
| JDBC             | Oracle JDBC (ojdbc11) |
| Database         | Oracle      |
| Build Tool       | Maven       |
| Server           | Apache Tomcat 10.1+ |
| Testing          | JUnit 5     |

---

## Project Structure

```
employee-leave-attendance-tracker/
├── pom.xml
├── README.md
├── .gitignore
├── database/
│   └── schema.sql
└── src/
    ├── main/
    │   ├── java/com/itc/employeeleaveattendance/
    │   │   ├── model/          Employee, LeaveRequest, LeaveBalance
    │   │   ├── dto/            PendingLeaveRequestDTO
    │   │   ├── constant/       Role, LeaveType, LeaveStatus
    │   │   ├── exception/      Custom exceptions
    │   │   ├── util/           DBUtil, DateUtil
    │   │   ├── dao/            DAO interfaces
    │   │   │   └── impl/      DAO implementations
    │   │   ├── service/        Service interfaces
    │   │   │   └── impl/      Service implementations
    │   │   ├── filter/         AuthenticationFilter, AuthorizationFilter
    │   │   └── controller/     Servlets
    │   ├── resources/
    │   │   └── application.properties
    │   └── webapp/
    │       ├── css/style.css
    │       ├── js/app.js
    │       └── WEB-INF/
    │           ├── web.xml
    │           └── views/
    │               ├── auth/login.jsp
    │               ├── employee/*.jsp
    │               ├── manager/dashboard.jsp
    │               └── common/*.jsp
    └── test/
        └── java/com/itc/employeeleaveattendance/
            └── util/DateUtilTest.java
```

---

## Database Setup

### Prerequisites

- Oracle Database (11g or later)
- Oracle SQL*Plus, SQL Developer, or compatible SQL client

### Steps

1. Connect to your Oracle database.
2. Run the schema script:
   ```sql
   @database/schema.sql
   ```
   This creates:
   - `employees` table (with sequences)
   - `leave_requests` table
   - `leave_balances` table
   - Sample data (2 managers, 5 employees)

### Sample Data Hierarchy

```
Manager 1: Priya Sharma (priya.sharma@company.com)
 ├── Employee 1: Anita Desai (anita.desai@company.com)
 ├── Employee 2: Vikram Patel (vikram.patel@company.com)
 └── Employee 3: Sneha Iyer (sneha.iyer@company.com)

Manager 2: Rajesh Kumar (rajesh.kumar@company.com)
 ├── Employee 4: Amit Verma (amit.verma@company.com)
 └── Employee 5: Kavita Nair (kavita.nair@company.com)
```

---

## Oracle Configuration

### 1. Update `application.properties`

Edit `src/main/resources/application.properties`:

```properties
db.url=jdbc:oracle:thin:@localhost:1521:xe
db.username=YOUR_ORACLE_USERNAME
db.password=YOUR_ORACLE_PASSWORD
```

### 2. Oracle JDBC Driver

The `ojdbc11` driver is included as a Maven dependency. If Maven cannot download it automatically, install it locally:

```bash
mvn install:install-file \
  -Dfile=/path/to/ojdbc11.jar \
  -DgroupId=com.oracle.database.jdbc \
  -DartifactId=ojdbc11 \
  -Dversion=23.3.0.23.09 \
  -Dpackaging=jar
```

Or add Oracle's Maven repository to your `pom.xml` or `settings.xml`.

---

## Maven Commands

```bash
# Clean the project
mvn clean

# Run tests
mvn test

# Build the WAR file
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

The WAR file is generated at: `target/employee-leave-tracker.war`

---

## Tomcat Deployment

### Option 1: Manual WAR Deployment

1. Build: `mvn clean package`
2. Copy `target/employee-leave-tracker.war` to Tomcat's `webapps/` directory.
3. Start Tomcat.
4. Access: `http://localhost:8080/employee-leave-tracker/login`

### Option 2: Tomcat Manager

1. Build the WAR.
2. Use Tomcat Manager to deploy the WAR file.

> **Note**: Requires **Tomcat 10.1+** (Jakarta Servlet 6.0).

---

## Sample Users

The login page provides a dropdown of seeded employees. Select an account to log in:

| Name           | Role     | Email                        |
|----------------|----------|------------------------------|
| Priya Sharma   | MANAGER  | priya.sharma@company.com     |
| Rajesh Kumar   | MANAGER  | rajesh.kumar@company.com     |
| Anita Desai    | EMPLOYEE | anita.desai@company.com      |
| Vikram Patel   | EMPLOYEE | vikram.patel@company.com     |
| Sneha Iyer     | EMPLOYEE | sneha.iyer@company.com       |
| Amit Verma     | EMPLOYEE | amit.verma@company.com       |
| Kavita Nair    | EMPLOYEE | kavita.nair@company.com      |

No passwords are required for the demo. Authentication is based on selecting a seeded employee.

---

## RBAC (Role-Based Access Control)

### URL Structure

| URL Pattern       | Required Role | Description              |
|-------------------|---------------|--------------------------|
| `/login`          | None          | Login page               |
| `/logout`         | None          | Logout                   |
| `/employee/*`     | EMPLOYEE      | Employee functionality   |
| `/manager/*`      | MANAGER       | Manager functionality    |

### Enforcement

1. **AuthenticationFilter**: Checks for valid session on protected URLs.
2. **AuthorizationFilter**: Verifies role matches URL pattern.
3. **Service Layer**: Validates manager-employee relationship for approve/reject.

Manually entering a manager URL as an employee results in a **403 Access Denied** page.

---

## Leave Approval Workflow

1. **Employee** submits leave request → status = `PENDING`.
2. **Manager** views pending requests from direct reports.
3. Manager clicks **Approve**:
   - Transaction begins
   - Verify request is PENDING
   - Verify manager is direct manager
   - Verify sufficient leave balance
   - Update status to APPROVED
   - Deduct working days from balance
   - Commit transaction
4. Manager clicks **Reject**:
   - Verify request is PENDING
   - Verify manager is direct manager
   - Update status to REJECTED
   - **No balance deduction**

---

## Leave Balance Workflow

- Default balances: Casual=12, Sick=12, Earned=15.
- Balance is checked **before** leave submission.
- Balance is deducted **only upon approval**.
- Balance is **not** deducted for rejected or pending requests.

---

## Testing

```bash
mvn test
```

### Test Coverage

- **DateUtilTest**: Working day calculations (weekday, weekend, cross-week, null inputs, invalid ranges).

---

## Important Assumptions

1. Managers do NOT apply for leave — they only manage direct reports' requests.
2. Authentication uses seeded-employee selection (no passwords, as the schema has none).
3. Default leave balances are Casual=12, Sick=12, Earned=15.
4. No attendance module — the project is a leave management system.
5. Jakarta Servlet 6.0 (Tomcat 10.1+) is used.
