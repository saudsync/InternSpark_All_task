# Employee API — Spring Boot CRUD

A RESTful Employee Management API built with Spring Boot, covering three internship tasks:
- **Task 1**: CRUD REST API with validation & global exception handling
- **Task 2**: MySQL database with Flyway migrations
- **Task 3**: Unit tests (Mockito + MockMvc) and structured logging (Logback)

## Tech Stack
- Java 17 · Spring Boot 3.2 · Spring Data JPA
- MySQL 8 · Flyway · HikariCP
- JUnit 5 · Mockito · JaCoCo (≥80% line coverage enforced)
- Logback (console + rolling file)

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+ running locally
- [GitHub CLI](https://cli.github.com/) (`gh`) — for the push script

## Quick Start

### 1. Database Setup
```sql
-- Run as MySQL root
CREATE DATABASE IF NOT EXISTS employee_db CHARACTER SET utf8mb4;
CREATE USER 'empapp'@'localhost' IDENTIFIED BY 'SecurePass123!';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP, REFERENCES
  ON employee_db.* TO 'empapp'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Run the App
```bash
mvn spring-boot:run
```
Flyway runs all 4 migrations automatically on startup.

### 3. Run Tests + Coverage
```bash
mvn verify
# Coverage report: target/site/jacoco/index.html
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees` | List all employees |
| GET | `/api/employees/{id}` | Get by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

## Example
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Johnson","email":"alice@company.com","department":"Engineering","salary":85000}'
```
