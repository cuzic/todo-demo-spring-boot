package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form object for task creation and editing.
 */
public class TaskForm {

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
