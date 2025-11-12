package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integration tests for TaskRepository.
 */
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testTaskRepository_Save_Success() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");

        // When
        Task savedTask = taskRepository.save(task);

        // Then
        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertFalse(savedTask.isCompleted());
    }

    @Test
    void testTaskRepository_FindAll_ReturnsAllTasks() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        entityManager.persist(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        entityManager.persist(task2);

        entityManager.flush();

        // When
        List<Task> tasks = taskRepository.findAll();

        // Then
        assertEquals(2, tasks.size());
    }

    @Test
    void testTaskRepository_FindById_ReturnsTask() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        Task savedTask = entityManager.persist(task);
        entityManager.flush();

        // When
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // Then
        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getTitle());
    }

    @Test
    void testTaskRepository_FindById_ReturnsEmptyWhenNotFound() {
        // Given
        Long nonExistentId = 99999L;

        // When
        Optional<Task> foundTask = taskRepository.findById(nonExistentId);

        // Then
        assertFalse(foundTask.isPresent());
    }

    @Test
    void testTaskRepository_Delete_RemovesTask() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        Task savedTask = entityManager.persist(task);
        entityManager.flush();

        Long taskId = savedTask.getId();

        // When
        taskRepository.deleteById(taskId);
        Optional<Task> deletedTask = taskRepository.findById(taskId);

        // Then
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void testTaskRepository_Count_ReturnsCorrectNumber() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        entityManager.persist(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        entityManager.persist(task2);

        entityManager.flush();

        // When
        long count = taskRepository.count();

        // Then
        assertEquals(2, count);
    }
}
