package com.example.demo.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for Task entity.
 */
class TaskTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testTaskEntity_Creation_Success() {
        // Given
        String title = "Test Task";

        // When
        Task task = new Task();
        task.setTitle(title);

        // Then
        assertNotNull(task);
        assertEquals(title, task.getTitle());
    }

    @Test
    void testTaskEntity_Validation_TitleNotBlank() {
        // Given
        Task task = new Task();
        task.setTitle("");

        // When
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("タイトルを入力してください")));
    }

    @Test
    void testTaskEntity_Validation_TitleMaxSize() {
        // Given
        String longTitle = "a".repeat(256);
        Task task = new Task();
        task.setTitle(longTitle);

        // When
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("255文字以内")));
    }

    @Test
    void testTaskEntity_DefaultValues_CompletedIsFalse() {
        // Given & When
        Task task = new Task();
        task.setTitle("Test Task");

        // Then
        assertNotNull(task.isCompleted());
        assertFalse(task.isCompleted());
    }

    @Test
    void testTaskEntity_GettersAndSetters_WorkCorrectly() {
        // Given
        String title = "Test Task";
        Boolean completed = true;

        // When
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(completed);

        // Then
        assertEquals(title, task.getTitle());
        assertEquals(completed, task.isCompleted());
    }

    @Test
    void testTaskEntity_ConstructorWithTitle_Success() {
        // Given
        String title = "Test Task with Constructor";

        // When
        Task task = new Task(title);

        // Then
        assertNotNull(task);
        assertEquals(title, task.getTitle());
        assertNotNull(task.isCompleted());
        assertFalse(task.isCompleted());
    }

    @Test
    void testTaskEntity_IdGetterSetter_WorkCorrectly() {
        // Given
        Long id = 100L;
        Task task = new Task();

        // When
        task.setId(id);

        // Then
        assertEquals(id, task.getId());
    }

    @Test
    void testTaskEntity_TimestampGettersSetter_WorkCorrectly() {
        // Given
        Task task = new Task("Test Task");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // When
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        // Then
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
    }
}
