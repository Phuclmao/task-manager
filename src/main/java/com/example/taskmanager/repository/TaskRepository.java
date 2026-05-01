package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.Task.Status;
import com.example.taskmanager.entity.Task.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerId(Long ownerId);

    List<Task> findByOwnerIdAndStatus(Long ownerId, Status status);

    List<Task> findByOwnerIdAndPriority(Long ownerId, Priority priority);

    Optional<Task> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT t FROM Task t WHERE t.owner.id = :ownerId " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority)")
    List<Task> findByOwnerIdWithFilters(
        @Param("ownerId") Long ownerId,
        @Param("status") Status status,
        @Param("priority") Priority priority
    );

    @Query("SELECT COUNT(t) FROM Task t WHERE t.owner.id = :ownerId AND t.status = :status")
    long countByOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") Status status);
}
