package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Task entity.
 * Provides CRUD operations for tasks.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository provides all basic CRUD operations
    // Additional custom query methods can be added here if needed
}
