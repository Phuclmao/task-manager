package com.example.taskmanager.service;

import com.example.taskmanager.dto.Dto.*;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.Task.Status;
import com.example.taskmanager.entity.Task.Priority;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Tạo task mới
    @Transactional
    public TaskResponse createTask(TaskRequest request, String username) {
        User owner = getUserByUsername(username);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : Status.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .dueDate(request.getDueDate())
                .owner(owner)
                .build();

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    // Lấy tất cả task của user hiện tại
    public List<TaskResponse> getAllTasks(String username, Status status, Priority priority) {
        User owner = getUserByUsername(username);
        List<Task> tasks = taskRepository.findByOwnerIdWithFilters(owner.getId(), status, priority);
        return tasks.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Lấy task theo ID
    public TaskResponse getTaskById(Long id, String username) {
        User owner = getUserByUsername(username);
        Task task = taskRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Task không tồn tại hoặc không có quyền truy cập"));
        return toResponse(task);
    }

    // Cập nhật task
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, String username) {
        User owner = getUserByUsername(username);
        Task task = taskRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Task không tồn tại hoặc không có quyền truy cập"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    // Cập nhật chỉ status
    @Transactional
    public TaskResponse updateStatus(Long id, Status status, String username) {
        User owner = getUserByUsername(username);
        Task task = taskRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Task không tồn tại hoặc không có quyền truy cập"));

        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    // Xóa task
    @Transactional
    public void deleteTask(Long id, String username) {
        User owner = getUserByUsername(username);
        Task task = taskRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Task không tồn tại hoặc không có quyền truy cập"));
        taskRepository.delete(task);
    }

    // Thống kê task của user
    public TaskStatsResponse getStats(String username) {
        User owner = getUserByUsername(username);
        long total = taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.TODO)
                + taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.IN_PROGRESS)
                + taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.DONE);

        return TaskStatsResponse.builder()
                .totalTasks(total)
                .todoCount(taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.TODO))
                .inProgressCount(taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.IN_PROGRESS))
                .doneCount(taskRepository.countByOwnerIdAndStatus(owner.getId(), Status.DONE))
                .build();
    }

    // ---- Helper Methods ----

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User không tìm thấy: " + username));
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .ownerUsername(task.getOwner().getUsername())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
