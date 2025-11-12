package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Task entity.
 * Provides CRUD operations for tasks.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Finds tasks by completion status, ordered by creation date descending.
     *
     * @param completed the completion status
     * @return list of tasks matching the completion status
     */
    List<Task> findByCompletedOrderByCreatedAtDesc(boolean completed);
}
