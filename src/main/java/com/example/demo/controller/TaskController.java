package com.example.demo.controller;

import com.example.demo.form.TaskForm;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing tasks.
 * Handles task list display, creation, and deletion.
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructor with dependency injection.
     *
     * @param taskService the task service
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "TaskService is a Spring-managed bean, not a mutable external object"
    )
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Displays the task list.
     *
     * @param model the model
     * @return the view name
     */
    @GetMapping
    public String getTaskList(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("taskForm", new TaskForm());
        return "tasks/list";
    }

    /**
     * Creates a new task.
     * Uses POST-Redirect-GET pattern to prevent duplicate submissions.
     *
     * @param taskForm the task form
     * @param bindingResult the binding result
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the redirect URL or view name
     */
    @PostMapping
    public String createTask(
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("tasks", taskService.getAllTasks());
            return "tasks/list";
        }

        taskService.createTask(taskForm.getTitle());
        redirectAttributes.addFlashAttribute("successMessage", "タスクを作成しました");
        return "redirect:/tasks";
    }

    /**
     * Deletes a task by ID.
     * Uses POST-Redirect-GET pattern.
     *
     * @param id the task ID
     * @param redirectAttributes the redirect attributes
     * @return the redirect URL
     */
    @PostMapping("/{id}/delete")
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public String deleteTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            taskService.deleteTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "タスクを削除しました");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "タスクが見つかりませんでした");
        }

        return "redirect:/tasks";
    }
}
