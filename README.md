# Employee Task & Attendance Management System

## 📌 Project Overview

The **Employee Task & Attendance Management System** is an internal application designed to manage employee attendance, task workflows, and performance tracking in a secure and centralized environment.

The system provides:

* Secure authentication using JWT
* Attendance tracking (check-in / check-out)
* Task assignment and monitoring
* Role-based access control
* Administrative dashboard and reporting

---

## 🏗️ Architecture

The application follows a **Layered Architecture**:

```
Controller → Service → Repository → Database
```

### Key Principles

* Separation of concerns
* Stateless authentication (JWT)
* Scalable and maintainable structure

---

## ⚙️ Tech Stack

* Java 21
* Spring Boot
* Spring Security (JWT)
* Hibernate / JPA
* MySQL
* Maven

---

## 🔐 Environment Variables (IMPORTANT)

This project **does NOT store sensitive data in code**.
You must configure environment variables before running.

---

### 🪟 Windows Setup (Using `setx`)

Run the following commands in **Command Prompt or Git Bash**:

```bash
setx DB_USERNAME "your_db_username"
setx DB_PASSWORD "your_db_password"
setx JWT_SECRET "your_very_secure_secret_key_32_chars_min"
setx ADMIN_EMAIL "admin_email"
setx ADMIN_PASSWORD "admin_password"
setx ADMIN_USERNAME "adminUser"
```

---

### ⚙️ Configuration Details

#### 🔐 Database & JWT Configuration

These variables are required to configure your database connection and authentication:

* `DB_USERNAME` → Your database username
* `DB_PASSWORD` → Your database password
* `JWT_SECRET` → Secret key used for JWT authentication (**must be at least 32 characters**)

#### 👤 Admin Account Initialization (First Run Only)

These variables are used to automatically create an admin account when the application runs for the first time:

* `ADMIN_EMAIL` → Admin email
* `ADMIN_PASSWORD` → Admin password
* `ADMIN_USERNAME` → Admin username

> ⚠️ **Note:** The admin account is created only during the initial run. Make sure to provide valid credentials.

---

### ⚠️ VERY IMPORTANT

After running `setx`:

👉 **Restart your IDE / terminal**
Otherwise variables will not be available to the application.

---

### 🐧 Linux / Mac (Optional)

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_secret
export ADMIN_EMAIL=admin_email
export ADMIN_PASSWORD=admin_password
export ADMIN_USERNAME=adminUser
```

---

## ⚙️ Application Configuration

Your `application.properties` uses environment variables:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

---

## 🗄️ Database Setup

1. Install MySQL
2. Create database:

```sql
CREATE DATABASE ems;
```

3. Update URL if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems
```

---

## 🚀 Running the Application

### Step 1 — Clone Repository

```bash
git clone https://github.com/Bittu949/employee-management-system.git
cd employee-management-system
```

---

### Step 2 — Build Project

```bash
mvnw clean install
```

---

### Step 3 — Run Application

```bash
mvnw spring-boot:run
```

---

### Step 4 — Access Application

```
http://localhost:8080
```

---

## 👤 Default Admin Creation

The system can automatically create an admin user using environment variables.

### ⚠️ Enable AdminInitializer

In `AdminInitializer.java`, uncomment:

```java
@Component
```

Run the application once → admin will be created.

Then disable it again to prevent duplicate creation.

---

## 🔐 Security Features

* JWT-based authentication
* BCrypt password hashing
* Role-based authorization (ADMIN / EMPLOYEE)
* Secure cookie-based session handling

---

## 📦 Modules

### Authentication

* Login
* JWT token generation
* Secure access control

### Attendance

* Check-in / Check-out
* Daily tracking
* Attendance reports

### Task Management

* Task assignment
* Status updates
* Deadline tracking

### Dashboard & Reports

* Performance metrics
* Attendance summary
* Task statistics

---

## 📡 API Details

For detailed information about all available endpoints, request/response formats, and usage, please refer to the API documentation file:

📄 API_DOCUMENTATION.md

💡 This file contains complete details about all APIs used in the application.

---

## 📋 Business Rules

* One check-in per day
* Checkout only after check-in
* Only admin can create tasks
* Only assigned user can update task
* Passwords are never stored in plain text

---

## 📄 License

Internal Use Only