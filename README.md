# Task Manager API

REST API quản lý công việc cá nhân, xây dựng bằng **Spring Boot 3 + Spring Security + JWT + MySQL**.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | MySQL 8 + Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Java | Java 17 |

## Cấu trúc project

```
src/main/java/com/example/taskmanager/
├── config/         # SecurityConfig, UserDetailsConfig
├── controller/     # AuthController, TaskController
├── dto/            # Request/Response DTOs
├── entity/         # User, Task (JPA Entities)
├── exception/      # GlobalExceptionHandler
├── repository/     # UserRepository, TaskRepository
├── security/       # JwtUtil, JwtAuthFilter
└── service/        # AuthService, TaskService
```

## Cài đặt và chạy

### 1. Yêu cầu
- Java 17+
- MySQL 8+
- Maven 3.8+

### 2. Tạo database
```sql
CREATE DATABASE task_manager_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Cấu hình `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Chạy project
```bash
mvn spring-boot:run
```
API sẽ chạy tại `http://localhost:8080`

---

## API Endpoints

### Auth (không cần token)

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập, nhận JWT |

### Tasks (cần JWT token trong header)

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/tasks` | Tạo task mới |
| GET | `/api/tasks` | Lấy tất cả task (có filter) |
| GET | `/api/tasks/{id}` | Lấy chi tiết task |
| PUT | `/api/tasks/{id}` | Cập nhật task |
| PATCH | `/api/tasks/{id}/status` | Cập nhật status |
| DELETE | `/api/tasks/{id}` | Xóa task |
| GET | `/api/tasks/stats` | Thống kê task |

---

## Ví dụ sử dụng (cURL)

### Đăng ký
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "phuc",
    "email": "phuc@example.com",
    "password": "123456",
    "fullName": "Cao Huu Phuc"
  }'
```

### Đăng nhập
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "phuc", "password": "123456"}'
```
→ Copy `token` từ response để dùng ở các request sau.

### Tạo task mới
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Học Spring Boot",
    "description": "Hoàn thành tutorial Spring Boot",
    "priority": "HIGH",
  }'
```

### Lấy task (filter theo status)
```bash
curl -X GET "http://localhost:8080/api/tasks?status=TODO&priority=HIGH" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Cập nhật status thành DONE
```bash
curl -X PATCH "http://localhost:8080/api/tasks/1/status?status=DONE" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Enum Values

**Task Status:** `TODO` | `IN_PROGRESS` | `DONE`

**Task Priority:** `LOW` | `MEDIUM` | `HIGH`

---

## Chạy Unit Test
```bash
mvn test
```
