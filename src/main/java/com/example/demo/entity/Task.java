package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Task entity representing a task in the todo list.
 *
 * <p>This entity maps to the "tasks" table in the database and represents
 * a single todo item with title, completion status, and timestamps.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Auto-generated ID using IDENTITY strategy</li>
 *   <li>Title validation (required, max 255 characters)</li>
 *   <li>Completion status tracking (defaults to false)</li>
 *   <li>Automatic creation and update timestamps</li>
 * </ul>
 *
 * @see TaskRepository
 * @see TaskService
 */
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * The unique identifier for this task.
     * Generated automatically using database IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title or description of the task.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 255, message = "タイトルは255文字以内で入力してください")
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * The completion status of the task.
     * Defaults to false (not completed) when a task is created.
     */
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * The timestamp when this task was created.
     * Automatically set by Hibernate on entity creation and cannot be updated.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when this task was last updated.
     * Automatically updated by Hibernate whenever the entity is modified.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public Task() {
        // Default constructor required by JPA
    }

    /**
     * Constructor with title.
     *
     * @param title the task title
     */
    public Task(String title) {
        this.title = title;
    }

    /**
     * Gets the task ID.
     *
     * @return the task ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the task ID.
     *
     * @param id the task ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the task title.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the task title.
     *
     * @param title the task title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the completed status.
     *
     * @return true if task is completed, false otherwise
     */
    public Boolean isCompleted() {
        return completed;
    }

    /**
     * Sets the completed status.
     *
     * @param completed the completed status
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return the last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
