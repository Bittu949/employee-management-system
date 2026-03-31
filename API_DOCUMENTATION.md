# 📡 Employee Task & Attendance Management System - API Documentation

---

# 🔐 AUTHENTICATION

## 🔹 Login
POST /auth/login

**Description:** Authenticates user and sets JWT token in HTTP-only cookie.

**Request Body (JSON):**
{
  "email": "user@example.com",
  "password": "password"
}

**Response:**
- 200 OK → JWT cookie set
- 401 → Invalid credentials

---

## 🔹 Get Current User Role
GET /auth/me

**Headers:**
- Cookie: jwt=<token>

**Response:**
{
  "role": "ADMIN | EMPLOYEE"
}

---

# 🏠 PUBLIC

## 🔹 Home Page
GET /

---

# 🛠 ADMIN MODULE

---

# 📊 DASHBOARD

## 🔹 Admin Dashboard
GET /admin/admin_dashboard

**Query Params:**
- page (int, default=0)
- size (int, default=10)

**Response (View Model):**
- totalTasks
- completedTasks
- pendingTasks
- todaysAttendancePercentage
- totalEmployee
- tasksDueThisWeek
- presentCount
- absentCount

---

## 🔹 Logout
GET /admin/logout

---

# 👥 USER MANAGEMENT

## 🔹 Add User Page
GET /admin/user/addUserPage

---

## 🔹 Create User
POST /admin/user/add

**Form Params:**
- fullName (required)
- email (required)
- userName
- password (required)
- confirmPassword (required)
- role (ADMIN | EMPLOYEE)
- status (ACTIVE | INACTIVE)

**Validation:**
- Password must match confirmPassword

---

## 🔹 Edit User Page
GET /admin/user/{userId}/editPage

**Path Param:**
- userId

---

## 🔹 Edit User
POST /admin/user/{userId}/edit

**Path Param:**
- userId

**Form Params:**
- fullName
- email
- username
- role
- status

---

## 🔹 Activate User
POST /admin/user/{userId}/activate

---

## 🔹 Deactivate User
POST /admin/user/{userId}/deactivate

---

## 🔹 Reset Password Page
GET /admin/user/{userId}/resetPasswordPage

---

## 🔹 Reset Password
POST /admin/user/{userId}/resetPassword

**Form Params:**
- newPassword (required)
- confirmPassword (required)

---

## 🔹 Get All Users
GET /admin/user/allUsers

**Query Params:**
- page
- size

---

## 🔹 Filter Users
GET /admin/user/filter

**Query Params:**
- search
- role
- status
- page
- size

**Response:**
- Paginated User list (JSON)

---

# 📋 TASK MANAGEMENT

## 🔹 Create Task Page
GET /admin/task/createPage

---

## 🔹 Create Task
POST /admin/task/create

**Form Params:**
- taskTitle (required)
- description
- deadline (YYYY-MM-DD, required)
- priority (LOW | MEDIUM | HIGH)
- status (PENDING | IN_PROGRESS | COMPLETED)

**Validation:**
- Deadline must be future date
- Title cannot be empty

---

## 🔹 Assign Task Page
GET /admin/task/assignPage

---

## 🔹 Assign Task
POST /admin/task/assign

**Form Params:**
- taskId
- userId

---

## 🔹 Task Preview
GET /admin/task/{taskId}/preview

**Response (JSON):**
{
  "id": 1,
  "taskTitle": "...",
  "deadline": "...",
  ...
}

---

## 🔹 Edit Task Page
GET /admin/task/{taskId}/editPage

---

## 🔹 Edit Task
POST /admin/task/edit

**Form Params:**
- taskId
- taskTitle
- description
- deadline
- priority
- status

---

## 🔹 View Task
GET /admin/task/{taskId}/view

---

## 🔹 Delete Task
POST /admin/task/{taskId}/delete

---

## 🔹 Reassign Task Page
GET /admin/task/{taskId}/reassignPage

---

## 🔹 Reassign Task
POST /admin/task/reassign

**Form Params:**
- taskId
- userId

---

## 🔹 Get All Tasks
GET /admin/task/allTasks

**Query Params:**
- page
- size

---

## 🔹 Filter Tasks
GET /admin/task/filter

**Query Params:**
- search
- status
- userId
- deadline
- page
- size

**Response:**
- Paginated Task list (JSON)

---

# 📅 ATTENDANCE MANAGEMENT

## 🔹 View Attendance Records
GET /admin/attendance/allRecords

---

## 🔹 Filter Attendance
GET /admin/attendance/filter

**Query Params:**
- name
- month (1-12)
- year
- status
- page
- size

---

## 🔹 Export Attendance
GET /admin/attendance/export

**Query Params:**
- search
- month
- year
- status

**Response:**
- CSV file download

---

# 📊 PERFORMANCE

## 🔹 Performance Dashboard
GET /admin/performance/details

---

## 🔹 Filter Performance
GET /admin/performance/filter

**Query Params:**
- search
- month
- year
- page
- size

---

## 🔹 Export Performance
GET /admin/performance/export

**Response:**
- CSV file download

---

# ⚙️ ADMIN SETTINGS

## 🔹 Settings Page
GET /admin/settings/page

---

## 🔹 Change Password
POST /admin/settings/changePassword

**Form Params:**
- currentPassword
- newPassword
- confirmPassword

---

# 👨‍💼 EMPLOYEE MODULE

---

# 📊 DASHBOARD

## 🔹 Employee Dashboard
GET /employee/employee_dashboard

---

## 🔹 Logout
GET /employee/logout

---

# 📅 ATTENDANCE

## 🔹 Attendance Page
GET /employee/attendance

---

## 🔹 Check In
POST /employee/attendance/check-in

**Response:**
"Checked In"

---

## 🔹 Check Out
POST /employee/attendance/check-out

---

## 🔹 Filter Attendance
GET /employee/attendance/filter

**Query Params:**
- status
- month
- year

---

# 📋 TASKS

## 🔹 My Tasks
GET /employee/tasks

---

## 🔹 Update Task Status
POST /employee/task/update-status

**Form Params:**
- taskId
- status

---

## 🔹 View Task
GET /employee/task/{id}

---

## 🔹 Filter Tasks
GET /employee/tasks/filter

**Query Params:**
- search
- status
- priority
- page
- size

---

# 👤 PROFILE

## 🔹 Profile Page
GET /employee/profile

---

## 🔹 Update Profile
POST /employee/profile/update

**Form Params:**
- fullName

---

# ⚙️ SETTINGS

## 🔹 Settings Page
GET /employee/settings

---

## 🔹 Change Password
POST /employee/change-password

**Request Body (JSON):**
{
  "currentPassword": "...",
  "newPassword": "...",
  "confirmPassword": "..."
}

---

# 🔐 SECURITY

- JWT stored in HTTP-only cookie
- Role-based access:
  - ADMIN
  - EMPLOYEE

---

# ⚙️ BUSINESS RULES

- One check-in per day
- Checkout only after check-in
- Deadline must be future date
- Only assigned user updates task
- Only admin manages users/tasks

---

# ✅ STATUS

✔ Fully implemented  
✔ Production-ready structure  
✔ Secure (JWT + validation)  
✔ Pagination + filtering supported  

---