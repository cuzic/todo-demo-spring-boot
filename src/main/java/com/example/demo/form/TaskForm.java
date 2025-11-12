package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form object for task creation and editing.
 *
 * <p>This form is used to capture and validate user input for task operations.
 * It uses Jakarta Bean Validation to ensure data integrity before processing.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *   <li>Title is required (cannot be blank)</li>
 *   <li>Title cannot exceed 255 characters</li>
 * </ul>
 *
 * <p>Usage examples:</p>
 * <pre>
 * // Creating a task
 * TaskForm form = new TaskForm();
 * form.setTitle("Buy groceries");
 *
 * // Editing a task
 * TaskForm form = new TaskForm(existingTask.getTitle());
 * </pre>
 *
 * @see TaskController
 * @see Task
 */
public class TaskForm {

    /**
     * The title of the task.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 255, message = "タイトルは255文字以内で入力してください")
    private String title;

    /**
     * Default constructor.
     */
    public TaskForm() {
        // Default constructor
    }

    /**
     * Constructor with title.
     *
     * @param title the task title
     */
    public TaskForm(String title) {
        this.title = title;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
