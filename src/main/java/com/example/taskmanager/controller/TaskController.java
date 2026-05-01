package com.example.taskmanager.controller;

import com.example.taskmanager.dto.Dto.*;
import com.example.taskmanager.entity.Task.Priority;
import com.example.taskmanager.entity.Task.Status;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * POST /api/tasks
     * Tạo task mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication auth) {
        TaskResponse task = taskService.createTask(request, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(task, "Tạo task thành công"));
    }

    /**
     * GET /api/tasks
     * Lấy tất cả task (có thể filter theo status và priority)
     * VD: GET /api/tasks?status=TODO&priority=HIGH
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            Authentication auth) {
        List<TaskResponse> tasks = taskService.getAllTasks(auth.getName(), status, priority);
        return ResponseEntity.ok(ApiResponse.success(tasks, "Lấy danh sách task thành công"));
    }

    /**
     * GET /api/tasks/{id}
     * Lấy chi tiết một task
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id,
            Authentication auth) {
        TaskResponse task = taskService.getTaskById(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success(task, "Lấy task thành công"));
    }

    /**
     * PUT /api/tasks/{id}
     * Cập nhật toàn bộ task
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Authentication auth) {
        TaskResponse task = taskService.updateTask(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success(task, "Cập nhật task thành công"));
    }

    /**
     * PATCH /api/tasks/{id}/status
     * Cập nhật chỉ status của task
     * VD: PATCH /api/tasks/1/status?status=DONE
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            Authentication auth) {
        TaskResponse task = taskService.updateStatus(id, status, auth.getName());
        return ResponseEntity.ok(ApiResponse.success(task, "Cập nhật trạng thái thành công"));
    }

    /**
     * DELETE /api/tasks/{id}
     * Xóa task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id,
            Authentication auth) {
        taskService.deleteTask(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa task thành công"));
    }

    /**
     * GET /api/tasks/stats
     * Thống kê task của user
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TaskStatsResponse>> getStats(Authentication auth) {
        TaskStatsResponse stats = taskService.getStats(auth.getName());
        return ResponseEntity.ok(ApiResponse.success(stats, "Lấy thống kê thành công"));
    }
}
