package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Task.Priority;
import com.example.taskmanager.entity.Task.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Dto {

    // ===================== AUTH =====================

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "Username không được để trống")
        @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
        private String username;

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        private String email;

        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
        private String password;

        private String fullName;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Username không được để trống")
        private String username;

        @NotBlank(message = "Password không được để trống")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String username;
        private String email;
        private String role;
        private String message;
    }

    // ===================== TASK =====================

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class TaskRequest {
        @NotBlank(message = "Tiêu đề không được để trống")
        private String title;

        private String description;

        private Status status;

        private Priority priority;

        private LocalDate dueDate;
    }

    @Data
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class TaskResponse {
        private Long id;
        private String title;
        private String description;
        private Status status;
        private Priority priority;
        private LocalDate dueDate;
        private String ownerUsername;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    // ===================== COMMON =====================

    @Data
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(T data, String message) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class TaskStatsResponse {
        private long totalTasks;
        private long todoCount;
        private long inProgressCount;
        private long doneCount;
    }
}
