# ✅ Task Manager API

Hệ thống quản lý công việc cá nhân được xây dựng bằng **Spring Boot 3**, tích hợp xác thực **JWT**, phân quyền theo role, và các nghiệp vụ như tạo, cập nhật, lọc và thống kê công việc.

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | SQL Server + Spring Data JPA / Hibernate |
| API Docs | Swagger / OpenAPI 3.0 |
| Build Tool | Maven |
| Java | Java 17 |
| Test | JUnit 5 + Mockito |

---

## ✨ Tính năng nổi bật

- 🔐 **JWT Authentication** — Đăng ký, đăng nhập, xác thực token
- 👥 **Phân quyền Admin/User** — Admin quản lý toàn bộ, User chỉ quản lý task của mình
- 📝 **Quản lý công việc** — Tạo, cập nhật, xóa, tìm kiếm task
- 🔍 **Lọc task** — Filter theo status, priority
- ⚡ **Cập nhật nhanh** — PATCH endpoint cập nhật chỉ status mà không cần gửi toàn bộ dữ liệu
- 📊 **Thống kê** — Đếm task theo trạng thái TODO / IN_PROGRESS / DONE
- 📖 **API Documentation** — Swagger UI đầy đủ, có thể test trực tiếp

---

## 🗄 Kiến trúc Database

```
users (1) ──────────── (n) tasks
```

**2 bảng:** `users`, `tasks`

---

## 🚀 Cài đặt và chạy

### Yêu cầu
- Java 17+
- SQL Server
- Maven 3.8+

### Bước 1 — Tạo database
```sql
CREATE DATABASE task_manager_db;
```

### Bước 2 — Cấu hình `application.properties`
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=task_manager_db;encrypt=false;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YOUR_PASSWORD
```

### Bước 3 — Chạy project
```bash
mvn spring-boot:run
```

### Bước 4 — Truy cập Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📡 API Endpoints

### 🔐 Auth
| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| POST | `/api/auth/register` | Đăng ký tài khoản | ❌ |
| POST | `/api/auth/login` | Đăng nhập, nhận JWT | ❌ |

### 📝 Tasks
| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| POST | `/api/tasks` | Tạo task mới | 🔐 User |
| GET | `/api/tasks` | Danh sách task (filter: status, priority) | 🔐 User |
| GET | `/api/tasks/{id}` | Chi tiết task | 🔐 User |
| PUT | `/api/tasks/{id}` | Cập nhật task | 🔐 User |
| PATCH | `/api/tasks/{id}/status` | Cập nhật nhanh status | 🔐 User |
| DELETE | `/api/tasks/{id}` | Xóa task | 🔐 User |
| GET | `/api/tasks/stats` | Thống kê task | 🔐 User |

---

## 💡 Ví dụ sử dụng

### 1. Đăng ký & Đăng nhập
```json
POST /api/auth/register
{
  "username": "phuc",
  "email": "phuc@example.com",
  "password": "123456",
  "fullName": "Cao Hữu Phúc"
}
```

### 2. Tạo task mới
```json
POST /api/tasks
Authorization: Bearer <token>
{
  "title": "Học Spring Boot",
  "description": "Hoàn thành project task manager",
  "priority": "HIGH",
  "dueDate": "2025-12-31"
}
```

### 3. Lọc task theo status và priority
```
GET /api/tasks?status=TODO&priority=HIGH
```

### 4. Cập nhật nhanh status
```
PATCH /api/tasks/1/status?status=DONE
```

### 5. Thống kê task
```
GET /api/tasks/stats
```
Response:
```json
{
  "totalTasks": 10,
  "todoCount": 4,
  "inProgressCount": 3,
  "doneCount": 3
}
```

---

## 🧪 Chạy Unit Test

```bash
mvn test
```

**4 test cases** bao gồm:
- Tạo task mới thành công
- Lấy danh sách task của user
- Xóa task không tồn tại → throw exception
- Cập nhật status task thành công

---

## 📋 Enum Values

| Enum | Values |
|------|--------|
| Task Status | `TODO` / `IN_PROGRESS` / `DONE` |
| Task Priority | `LOW` / `MEDIUM` / `HIGH` |

---
