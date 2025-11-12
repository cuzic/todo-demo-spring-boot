package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing tasks.
 * Provides business logic for CRUD operations on tasks.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructor with dependency injection.
     *
     * @param taskRepository the task repository
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates a new task with the given title.
     *
     * @param title the task title
     * @return the created task
     */
    public Task createTask(String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    /**
     * Retrieves all tasks.
     *
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task ID
     * @return the task
     * @throws RuntimeException if task is not found
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    /**
     * Updates a task's title.
     *
     * @param id the task ID
     * @param title the new title
     * @return the updated task
     * @throws RuntimeException if task is not found
     */
    public Task updateTask(Long id, String title) {
        Task task = getTaskById(id);
        task.setTitle(title);
        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the task ID
     * @throws RuntimeException if task is not found
     */
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.deleteById(task.getId());
    }

    /**
     * Toggles the completion status of a task.
     *
     * @param id the task ID
     * @return the updated task
     * @throws RuntimeException if task is not found
     */
    public Task toggleTaskCompletion(Long id) {
        Task task = getTaskById(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }
}
