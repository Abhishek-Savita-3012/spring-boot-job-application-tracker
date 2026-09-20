# Job Application Tracking System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/Authentication-JWT-black.svg)](https://jwt.io/)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
[![Swagger](https://img.shields.io/badge/API%20Docs-Swagger-green.svg)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

A production-ready **Spring Boot REST API** for securely managing and tracking job applications throughout the complete job-search lifecycle.

The system allows authenticated users to manage job applications, application statuses, interview rounds, notes, resume references, follow-up reminders, archived applications, and dashboard analytics while maintaining strict user-level resource ownership.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Domain Model](#domain-model)
- [Authentication and Security](#authentication-and-security)
- [API Response Format](#api-response-format)
- [API Endpoints](#api-endpoints)
- [Search, Filtering, Sorting and Pagination](#search-filtering-sorting-and-pagination)
- [Dashboard Analytics](#dashboard-analytics)
- [Validation and Exception Handling](#validation-and-exception-handling)
- [Reminder Scheduler](#reminder-scheduler)
- [Testing](#testing)
- [Swagger / OpenAPI](#swagger--openapi)
- [Local Setup](#local-setup)
- [Environment Variables](#environment-variables)
- [Running with Maven](#running-with-maven)
- [Running Tests](#running-tests)
- [Docker](#docker)
- [Deployment](#deployment)
- [Important Design Decisions](#important-design-decisions)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

# Overview

Tracking job applications across multiple companies, portals, emails, resumes, interviews, and follow-ups can quickly become difficult.

This project provides a centralized backend system where users can securely manage their complete job-application workflow.

Each authenticated user can:

- create and manage job applications
- track application status changes
- maintain application status history
- record interview rounds
- add application-specific notes
- maintain resume references
- attach resumes to job applications
- create follow-up reminders
- archive and restore applications
- search, filter, sort, and paginate applications
- view dashboard analytics

All protected resources are isolated by authenticated user ownership.

---

# Key Features

## Authentication

- User registration
- User login
- BCrypt password hashing
- JWT token generation
- JWT-based stateless authentication
- Current authenticated user resolution
- Protected API endpoints

## Job Application Management

- Create job applications
- Retrieve applications
- Update application details
- Delete applications
- Secure ownership validation
- Update application status
- Track status history

## Application Organization

- Keyword search
- Filter by application status
- Filter by work mode
- Filter by employment type
- Sort by supported fields
- Paginated responses
- Archive applications
- Restore archived applications

## Interview Tracking

- Create interview rounds
- Update interview details
- Track interview outcomes
- Delete interview rounds
- Retrieve interviews for a specific application

## Application Notes

- Add notes to applications
- Retrieve application notes
- Update notes
- Delete notes

## Resume Management

- Create resume references
- Store resume metadata
- Attach resumes to job applications
- Detach resumes from applications
- Secure resume ownership validation

## Follow-Up Reminders

- Create follow-up reminders
- Reschedule reminders
- Mark reminders as completed
- Cancel reminders
- Scheduled processing of due reminders

## Dashboard Analytics

- Total active applications
- Total archived applications
- Applications submitted this month
- Upcoming interviews
- Pending reminders
- Offer count
- Rejection count
- Applications grouped by status
- Applications grouped by work mode
- Applications grouped by employment type

## API Quality

- Request/response DTOs
- Centralized entity-to-DTO mapping
- Bean Validation
- Global exception handling
- Standardized success responses
- Standardized error responses
- Swagger / OpenAPI documentation

## Testing

- JUnit 5
- Mockito
- Service unit testing
- Integration testing
- Security and ownership testing
- MockMvc-based API testing
- H2 test database

---

# Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| REST API | Spring Web |
| Security | Spring Security |
| Authentication | JWT |
| Password Hashing | BCrypt |
| ORM | Hibernate / JPA |
| Persistence | Spring Data JPA |
| Database | MySQL |
| Test Database | H2 |
| Validation | Jakarta Bean Validation |
| API Documentation | Springdoc OpenAPI / Swagger UI |
| Unit Testing | JUnit 5 |
| Mocking | Mockito |
| Integration Testing | MockMvc |
| Build Tool | Maven |
| Containerization | Docker |
| Deployment | Render |
| Version Control | Git / GitHub |

---

# Architecture

The application follows a layered backend architecture.

```text
                    ┌─────────────────────┐
                    │       Client        │
                    │ Swagger / Postman   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Spring Security   │
                    │ JWT Authentication  │
                    │       Filter        │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controller      │
                    │      Layer          │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Service        │
                    │       Layer         │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Repository       │
                    │       Layer         │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       MySQL         │
                    │      Database       │
                    └─────────────────────┘
```

### Request Flow

```text
HTTP Request
    ↓
JwtAuthenticationFilter
    ↓
Spring SecurityContext
    ↓
Controller
    ↓
DTO Validation
    ↓
Service
    ↓
Repository
    ↓
MySQL
    ↓
Mapper
    ↓
Response DTO
    ↓
ApiResponse<T>
```

---

# Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/abhishek/jobtracker/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── exception/
│   │       ├── mapper/
│   │       ├── repository/
│   │       ├── security/
│   │       ├── service/
│   │       └── specification/
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    ├── java/
    │   └── com/abhishek/jobtracker/
    │       ├── service/
    │       └── integration/
    │
    └── resources/
        └── application-test.properties
```

## Package Responsibilities

| Package | Responsibility |
|---|---|
| `config` | Spring Security, OpenAPI and application configuration |
| `controller` | REST API endpoints |
| `dto` | Request and response objects |
| `entity` | JPA entity models |
| `exception` | Custom exceptions and global exception handling |
| `mapper` | Entity-to-response DTO mapping |
| `repository` | Spring Data JPA repositories |
| `security` | JWT generation, validation and authentication |
| `service` | Business logic |
| `specification` | Dynamic search and filtering |

---

# Domain Model

```text
User
 │
 └── JobApplication
      │
      ├── StatusHistory
      │
      ├── InterviewRound
      │
      ├── ApplicationNote
      │
      ├── FollowUpReminder
      │
      └── Resume
```

Every job application belongs to a single authenticated user.

Resources such as interviews, notes, reminders, and resumes are also validated against the authenticated user's ownership.

---

# Authentication and Security

The backend uses **stateless JWT authentication**.

## Authentication Flow

```text
User Registration
      ↓
Password hashed with BCrypt
      ↓
User stored in database


User Login
      ↓
Email + Password validation
      ↓
JWT generated
      ↓
Token returned to client


Protected Request
      ↓
Authorization: Bearer <JWT>
      ↓
JwtAuthenticationFilter
      ↓
JWT validation
      ↓
SecurityContext populated
      ↓
Protected endpoint accessed
```

## Authorization Header

Protected requests require:

```http
Authorization: Bearer <JWT_TOKEN>
```

## Ownership Security

The API does not trust a user ID supplied by the client.

The current user is obtained from the Spring Security context.

Resource access uses ownership-aware repository queries such as:

```java
findByIdAndUser_Id(applicationId, currentUserId)
```

This prevents a user from accessing another user's resources simply by guessing database IDs.

For security-sensitive resources, inaccessible records are returned as:

```text
404 Not Found
```

rather than exposing whether another user's resource exists.

---

# API Response Format

## Successful Response

```json
{
  "success": true,
  "message": "Job application fetched successfully",
  "data": {
    "id": 1,
    "company": "Example Company",
    "role": "Java Backend Developer"
  },
  "timestamp": "2026-09-20T12:00:00"
}
```

## Validation Error Response

```json
{
  "success": false,
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "company": "Company is required",
    "role": "Role is required"
  },
  "path": "/api/applications",
  "timestamp": "2026-09-20T12:00:00"
}
```

## Not Found Response

```json
{
  "success": false,
  "status": 404,
  "message": "Job application not found",
  "errors": null,
  "path": "/api/applications/999",
  "timestamp": "2026-09-20T12:00:00"
}
```

---

# API Endpoints

> Protected endpoints require a valid JWT access token.

## User Authentication

| Method | Endpoint | Authentication | Description |
|---|---|---|---|
| `POST` | `/api/users/register` | Public | Register a new user |
| `POST` | `/api/users/login` | Public | Authenticate and receive JWT |
| `GET` | `/api/users/me` | Required | Get current authenticated user |

---

## Job Applications

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/applications` | Create job application |
| `GET` | `/api/applications` | Retrieve applications with search/filter/pagination |
| `GET` | `/api/applications/{id}` | Retrieve application by ID |
| `PUT` | `/api/applications/{id}` | Update application |
| `DELETE` | `/api/applications/{id}` | Permanently delete application |
| `PATCH` | `/api/applications/{id}/status` | Update application status |
| `PATCH` | `/api/applications/{id}/archive` | Archive application |
| `PATCH` | `/api/applications/{id}/restore` | Restore archived application |

---

## Application Status History

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/applications/{id}/status-history` | Retrieve status-change history |

Status changes are automatically recorded whenever the dedicated status update operation is used.

---

## Application Notes

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/applications/{applicationId}/notes` | Add note |
| `GET` | `/api/applications/{applicationId}/notes` | Retrieve all notes |
| `GET` | `/api/applications/{applicationId}/notes/{noteId}` | Retrieve note |
| `PUT` | `/api/applications/{applicationId}/notes/{noteId}` | Update note |
| `DELETE` | `/api/applications/{applicationId}/notes/{noteId}` | Delete note |

---

## Interview Rounds

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/applications/{applicationId}/interviews` | Add interview round |
| `GET` | `/api/applications/{applicationId}/interviews` | Retrieve interview rounds |
| `GET` | `/api/applications/{applicationId}/interviews/{interviewId}` | Retrieve interview round |
| `PUT` | `/api/applications/{applicationId}/interviews/{interviewId}` | Update interview round |
| `DELETE` | `/api/applications/{applicationId}/interviews/{interviewId}` | Delete interview round |

### Interview Outcomes

```text
PENDING
PASSED
FAILED
CANCELLED
```

### Interview Types

```text
PHONE_SCREEN
CODING_ASSESSMENT
TECHNICAL
BEHAVIORAL
MANAGERIAL
HR
OTHER
```

---

## Resume Management

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/resumes` | Create resume reference |
| `GET` | `/api/resumes` | Retrieve user's resumes |
| `GET` | `/api/resumes/{id}` | Retrieve resume |
| `PATCH` | `/api/applications/{applicationId}/resume/{resumeId}` | Attach resume |
| `DELETE` | `/api/applications/{applicationId}/resume` | Detach resume |

Resume records store metadata and external references rather than binary file contents.

---

## Follow-Up Reminders

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/applications/{applicationId}/reminders` | Create reminder |
| `GET` | `/api/applications/{applicationId}/reminders` | Retrieve reminders |
| `PUT` | `/api/applications/{applicationId}/reminders/{reminderId}` | Update/reschedule reminder |
| `PATCH` | `/api/applications/{applicationId}/reminders/{reminderId}/complete` | Complete reminder |
| `PATCH` | `/api/applications/{applicationId}/reminders/{reminderId}/cancel` | Cancel reminder |
| `DELETE` | `/api/applications/{applicationId}/reminders/{reminderId}` | Delete reminder |

### Reminder Statuses

```text
PENDING
COMPLETED
CANCELLED
```

---

## Dashboard

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard` | Retrieve authenticated user's analytics |

---

# Application Statuses

```text
SAVED
APPLIED
SCREENING
INTERVIEW
OFFER
REJECTED
WITHDRAWN
```

---

# Employment Types

```text
FULL_TIME
PART_TIME
INTERNSHIP
CONTRACT
FREELANCE
```

---

# Work Modes

```text
ONSITE
HYBRID
REMOTE
```

---

# Search, Filtering, Sorting and Pagination

The main applications endpoint supports dynamic querying.

## Example

```http
GET /api/applications?keyword=java&status=APPLIED&workMode=REMOTE&page=0&size=10&sortBy=createdAt&direction=desc
```

## Supported Parameters

| Parameter | Description |
|---|---|
| `keyword` | Search company, role, location or source |
| `status` | Filter by application status |
| `workMode` | Filter by work mode |
| `employmentType` | Filter by employment type |
| `archived` | Retrieve active or archived applications |
| `page` | Zero-based page number |
| `size` | Number of records per page |
| `sortBy` | Field used for sorting |
| `direction` | `asc` or `desc` |

## Supported Sort Fields

```text
createdAt
updatedAt
company
role
salary
appliedDate
deadline
status
archivedAt
```

Maximum supported page size:

```text
100
```

---

# Dashboard Analytics

The dashboard aggregates application data for the authenticated user.

Example metrics include:

```text
Total Active Applications
Total Archived Applications
Applications This Month
Upcoming Interviews
Pending Reminders
Offers
Rejections
```

Applications are also grouped by:

```text
Application Status
Work Mode
Employment Type
```

---

# Validation and Exception Handling

The project uses Jakarta Bean Validation for request validation.

Examples include:

```text
@NotBlank
@NotNull
@Email
@Size
@PositiveOrZero
@Future
@PastOrPresent
@Pattern
@AssertTrue
```

A centralized:

```java
@RestControllerAdvice
```

handles application exceptions consistently.

## HTTP Status Mapping

| Situation | Status |
|---|---|
| Validation failure | `400 Bad Request` |
| Invalid request | `400 Bad Request` |
| Invalid pagination | `400 Bad Request` |
| Invalid sorting | `400 Bad Request` |
| Invalid credentials | `401 Unauthorized` |
| Missing authentication | `401 Unauthorized` |
| Resource not found | `404 Not Found` |
| Duplicate email | `409 Conflict` |
| Unexpected backend error | `500 Internal Server Error` |

Internal stack traces are logged on the server and are not exposed in API responses.

---

# Reminder Scheduler

Spring Scheduling processes pending reminders.

```text
Pending Reminder
      ↓
Scheduled job checks due reminders
      ↓
Notification service processes reminder
      ↓
notifiedAt timestamp updated
```

The scheduler delay can be configured using an environment variable.

---

# Transaction Management

Service-level transaction boundaries are used with Spring's:

```java
@Transactional
```

Read operations use:

```java
@Transactional(readOnly = true)
```

where appropriate.

This keeps database operations consistent and allows lazy JPA relationships to be safely accessed while mapping entities into response DTOs.

---

# Testing

The project includes both isolated unit tests and full application integration tests.

## Unit Tests

JUnit 5 and Mockito are used to test individual services without starting Spring or connecting to MySQL.

Examples include:

```text
Successful registration
Duplicate email handling
Invalid credentials
Missing users
Application ownership checks
Archive behavior
Restore behavior
```

## Integration Tests

Integration tests run the real application layers together:

```text
MockMvc
   ↓
Controller
   ↓
Validation
   ↓
Security
   ↓
Service
   ↓
Repository
   ↓
H2 Test Database
```

Examples include:

```text
Register → Persist User
Register → Login → JWT
JWT → Create Application
Create → Retrieve Application
Invalid Registration → 400
Duplicate Email → 409
```

## Security Tests

Security testing verifies scenarios such as:

```text
Unauthenticated protected request → blocked
Invalid JWT → blocked
Authenticated access → allowed
User A accessing User B resource → blocked
Cross-user updates → blocked
```

---

# Swagger / OpenAPI

Interactive API documentation is generated using Springdoc OpenAPI.

## Local Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

## OpenAPI JSON

```text
http://localhost:8080/v3/api-docs
```

Swagger supports JWT authentication through the **Authorize** button.

Paste the generated JWT token into the authorization dialog and Swagger will automatically send:

```http
Authorization: Bearer <token>
```

for protected requests.

---

# Local Setup

## Prerequisites

Make sure the following are installed:

- Java 21
- MySQL 8+
- Git

Maven installation is optional because the repository includes the Maven Wrapper.

---

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPOSITORY_NAME.git
```

Move into the project:

```bash
cd YOUR_REPOSITORY_NAME
```

---

## 2. Create MySQL Database

Open MySQL Workbench or the MySQL CLI.

```sql
CREATE DATABASE job_tracker_db;
```

---

## 3. Configure Environment Variables

The application expects database and JWT configuration through environment variables.

### Windows PowerShell

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/job_tracker_db"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:JWT_SECRET="your_strong_jwt_secret"
```

### Linux / macOS

```bash
export DB_URL="jdbc:mysql://localhost:3306/job_tracker_db"
export DB_USERNAME="root"
export DB_PASSWORD="your_mysql_password"
export JWT_SECRET="your_strong_jwt_secret"
```

---

# Environment Variables

| Variable | Required | Description |
|---|---|---|
| `DB_URL` | Yes | JDBC database URL |
| `DB_USERNAME` | Yes | Database username |
| `DB_PASSWORD` | Yes | Database password |
| `JWT_SECRET` | Yes | Secret used for JWT signing |
| `JWT_EXPIRATION` | No | JWT lifetime in milliseconds |
| `REMINDER_SCHEDULER_DELAY` | No | Reminder scheduler delay |
| `PORT` | No | Application server port |

Example:

```env
DB_URL=jdbc:mysql://localhost:3306/job_tracker_db
DB_USERNAME=root
DB_PASSWORD=your_database_password
JWT_SECRET=replace_with_a_secure_secret
JWT_EXPIRATION=3600000
REMINDER_SCHEDULER_DELAY=60000
PORT=8080
```

> Never commit real credentials or production secrets to GitHub.

---

# Running with Maven

## Windows

```powershell
.\mvnw.cmd spring-boot:run
```

## Linux / macOS

```bash
./mvnw spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

---

# Running Tests

## Windows

```powershell
.\mvnw.cmd clean test
```

## Linux / macOS

```bash
./mvnw clean test
```

Expected result:

```text
BUILD SUCCESS
```

---

# Building the Application

## Windows

```powershell
.\mvnw.cmd clean package
```

## Linux / macOS

```bash
./mvnw clean package
```

The generated JAR will be created inside:

```text
target/
```

Example:

```text
target/job-tracker-0.0.1-SNAPSHOT.jar
```

Run the packaged application using:

```bash
java -jar target/job-tracker-0.0.1-SNAPSHOT.jar
```

---

# Docker

The application can be run together with MySQL using Docker Compose.

The Docker setup contains two services:

- `mysql` — MySQL 8 database
- `backend` — Spring Boot application

The backend runs with the `docker` Spring profile and connects to the MySQL container through Docker's internal network.

## Docker Architecture

```text
Host Machine
     |
     |---- localhost:8081
     |          |
     |          v
     |   Spring Boot Container
     |      backend:8080
     |          |
     |          | jdbc:mysql://mysql:3306/job_tracker_db
     |          v
     |     MySQL Container
     |       mysql:3306
     |
     |---- localhost:3308
                |
                v
          MySQL Container
```

Inside the Docker network, the backend uses:

```text
mysql:3306
```

to connect to the database.

From the host machine, MySQL is available on:

```text
localhost:3308
```

and the Spring Boot API is available on:

```text
http://localhost:8081
```

---

## Docker Environment Variables

Before starting the containers, configure the following environment variables.

Create a `.env` file in the project root:

```env
MYSQL_USER=jobtracker_user
MYSQL_PASSWORD=your_mysql_password
MYSQL_ROOT_PASSWORD=your_mysql_root_password

JWT_SECRET=your_secure_jwt_secret
```

Do not commit the real `.env` file to GitHub.

Make sure `.env` is included in `.gitignore`:

```gitignore
.env
```

---

## Docker Spring Profile

Docker Compose activates the Spring profile:

```text
docker
```

using:

```yaml
SPRING_PROFILES_ACTIVE: docker
```

The Docker-specific configuration is stored in:

```text
src/main/resources/application-docker.properties
```

The database configuration used inside Docker is:

```properties
spring.datasource.url=jdbc:mysql://mysql:3306/job_tracker_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:3600000}

reminder.scheduler.delay=${REMINDER_SCHEDULER_DELAY:60000}

server.port=8080
```

The hostname:

```text
mysql
```

corresponds to the MySQL service name defined in `compose.yaml`.

---

## Start the Application with Docker

Build and start all services:

```bash
docker compose up --build
```

To run the containers in the background:

```bash
docker compose up --build -d
```

Docker Compose will:

```text
1. Start the MySQL 8 container
2. Wait until MySQL becomes healthy
3. Start the Spring Boot backend
4. Activate the "docker" Spring profile
5. Connect the backend to MySQL
```

The backend waits for the MySQL health check before starting.

---

## Access the Application

After both containers are running:

### Backend API

```text
http://localhost:8081
```

### Swagger UI

```text
http://localhost:8081/swagger-ui.html
```

### OpenAPI JSON

```text
http://localhost:8081/v3/api-docs
```

### MySQL from Host Machine

```text
Host: localhost
Port: 3308
Database: job_tracker_db
```

The MySQL container itself runs on port:

```text
3306
```

but Docker maps it to:

```text
3308
```

on the host machine.

---

## Check Running Containers

```bash
docker compose ps
```

You should see containers similar to:

```text
job-tracker-backend
job-tracker-mysql
```

---

## View Container Logs

### Backend Logs

```bash
docker compose logs -f backend
```

### MySQL Logs

```bash
docker compose logs -f mysql
```

### All Logs

```bash
docker compose logs -f
```

---

## Stop the Containers

Stop the application:

```bash
docker compose down
```

This removes the containers and Docker network but preserves the MySQL data volume.

---

## Reset the Docker Database

MySQL data is stored in a persistent Docker volume:

```text
mysql_data
```

Therefore, normal:

```bash
docker compose down
```

does not delete database data.

To completely remove the containers **and the MySQL data volume**:

```bash
docker compose down -v
```

> Warning: this permanently deletes the MySQL data stored in the Docker volume.

After removing the volume, start the application again using:

```bash
docker compose up --build
```

and a fresh database will be created.

---

## Rebuild After Code Changes

If the backend source code changes, rebuild the containers using:

```bash
docker compose up --build
```

For a completely fresh rebuild:

```bash
docker compose build --no-cache
docker compose up
```

---

## Docker Port Mapping

| Service | Container Port | Host Port |
|---|---:|---:|
| Spring Boot Backend | `8080` | `8081` |
| MySQL | `3306` | `3308` |

---

## Docker Service Configuration

The Docker Compose environment passes the following variables to the Spring Boot container:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
JWT_EXPIRATION
REMINDER_SCHEDULER_DELAY
```

The database username and password are derived from:

```text
MYSQL_USER
MYSQL_PASSWORD
```

defined in the project's `.env` file.

---

## Docker Data Persistence

The MySQL database uses a named Docker volume:

```text
mysql_data
```

mounted at:

```text
/var/lib/mysql
```

This keeps application data persistent even when the containers are restarted or recreated.

---

## Useful Docker Commands

| Action | Command |
|---|---|
| Build and start | `docker compose up --build` |
| Start in background | `docker compose up -d` |
| Check containers | `docker compose ps` |
| Backend logs | `docker compose logs -f backend` |
| MySQL logs | `docker compose logs -f mysql` |
| Stop containers | `docker compose down` |
| Stop and delete DB volume | `docker compose down -v` |
| Rebuild without cache | `docker compose build --no-cache` |

---

# Deployment

The backend is deployed using **Render** with an external MySQL-compatible database.

Production configuration is injected through environment variables rather than hard-coded into the repository.

## Live API

```text
https://spring-boot-job-application-tracker.onrender.com
```

## Swagger UI

```text
https://spring-boot-job-application-tracker.onrender.com/swagger-ui.html
```

## Production Configuration

The deployment environment provides:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
PORT
```

Sensitive values are never stored directly in source control.

---

# Important Design Decisions

## 1. Layered Architecture

The project separates:

```text
Controller
Service
Repository
Entity
DTO
```

responsibilities to keep the codebase easier to maintain and test.

---

## 2. DTO-Based API Design

JPA entities are not exposed directly through the public REST API.

DTOs provide separation between:

```text
Database Model
      and
Public API Contract
```

---

## 3. Ownership-Aware Queries

Resources are queried using both the resource ID and authenticated user ID.

Example:

```java
findByIdAndUser_Id(id, currentUserId)
```

This reduces the risk of insecure direct object references.

---

## 4. Stateless JWT Authentication

The server does not maintain user login sessions.

Each protected request contains:

```http
Authorization: Bearer <JWT>
```

which is validated independently.

---

## 5. Archive Instead of Delete

Archiving is separate from permanent deletion.

```text
Archive
→ reversible
→ hidden from active application list

Delete
→ permanent
```

---

## 6. Status History

Application status changes are stored separately so the system preserves the progression of an application over time.

Example:

```text
SAVED
  ↓
APPLIED
  ↓
SCREENING
  ↓
INTERVIEW
  ↓
OFFER
```

---

## 7. Dynamic Filtering with JPA Specifications

Spring Data JPA Specifications allow filters to be combined dynamically without creating repository methods for every possible filter combination.

---

## 8. Transaction Boundaries

Service-level transaction management ensures related JPA data remains available while business logic and DTO mapping are executed.

This also avoids lazy-loading problems caused by accessing Hibernate proxies outside an active persistence context.

---

## 9. Centralized Exception Handling

Expected application exceptions are handled centrally rather than with repeated `try/catch` blocks inside controllers.

---

## 10. Environment-Based Configuration

Secrets and production settings are injected through environment variables.

This keeps credentials separate from the source repository.

---

# Example Application Workflow

```text
Register
   ↓
Login
   ↓
Receive JWT
   ↓
Create Job Application
   ↓
Update Application Status
   ↓
Track Status History
   ↓
Add Interview Rounds
   ↓
Add Notes
   ↓
Attach Resume
   ↓
Create Follow-Up Reminder
   ↓
View Dashboard Analytics
   ↓
Archive Completed Application
```

---

# Future Improvements

Possible future enhancements include:

- Refresh token support
- Password reset functionality
- Email verification
- Real email reminder notifications
- Cloud resume file storage
- Flyway database migrations
- Testcontainers with MySQL
- CI/CD using GitHub Actions
- Role-based administrative APIs
- Advanced analytics
- Job application activity timeline
- Frontend web application
- Export applications to CSV/PDF
- Calendar integration
- Notification preferences

---

# Repository Security

The following files or values should never be committed:

```text
.env
database passwords
JWT secrets
production credentials
API keys
```

Example `.gitignore` entries:

```gitignore
target/
.idea/
*.iml
*.log

.env
.env.local
.env.production

application-local.properties
```

---

# Git Workflow

Example development workflow:

```bash
git checkout -b feature/example-feature
git add .
git commit -m "Add example feature"
git push origin feature/example-feature
```

Final changes can then be merged into the main branch.

---

# Useful Commands

| Action | Command |
|---|---|
| Run application | `./mvnw spring-boot:run` |
| Run tests | `./mvnw clean test` |
| Build project | `./mvnw clean package` |
| Build Docker image | `docker build -t job-application-tracker .` |
| Check Git status | `git status` |
| View Swagger | `http://localhost:8080/swagger-ui.html` |

For Windows, replace:

```text
./mvnw
```

with:

```text
.\mvnw.cmd
```

---

# What I Learned

This project provided practical experience with:

- Spring Boot application development
- REST API design
- layered backend architecture
- Spring Data JPA
- Hibernate relationships
- database modeling
- Spring Security
- JWT authentication
- BCrypt password hashing
- user-level authorization
- DTO design
- entity mapping
- Bean Validation
- global exception handling
- pagination
- sorting
- filtering
- JPA Specifications
- transaction management
- lazy loading
- scheduled tasks
- Swagger/OpenAPI
- JUnit
- Mockito
- integration testing
- security testing
- Docker
- environment-based configuration
- deployment
- debugging production issues

---

# Author

**Abhishek Savita**

Java / Spring Boot Backend Developer

GitHub:

```text
https://github.com/Abhishek-Savita-3012
```


---

⭐ If you found this project useful, consider giving the repository a star.