package com.example.demo.controller;

import com.example.demo.entity.Task;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing tasks.
 * Handles task list display, creation, deletion, completion toggle, and editing.
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private static final String REDIRECT_TASKS = "redirect:/tasks";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String TASK_NOT_FOUND = "タスクが見つかりませんでした";
    private static final String PMD_SUPPRESS = "PMD.AvoidCatchingGenericException";
    private static final String FILTER_ACTIVE = "active";
    private static final String FILTER_COMPLETED = "completed";

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
     * Displays the task list with optional filtering.
     *
     * @param filter the filter parameter ("active", "completed", or null for all)
     * @param model the model
     * @return the view name
     */
    @GetMapping
    public String getTaskList(
            @RequestParam(required = false) String filter,
            Model model) {
        List<Task> tasks;
        if (FILTER_ACTIVE.equals(filter)) {
            tasks = taskService.getActiveTasks();
        } else if (FILTER_COMPLETED.equals(filter)) {
            tasks = taskService.getCompletedTasks();
        } else {
            tasks = taskService.getAllTasks();
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
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
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "タスクを作成しました");
        return REDIRECT_TASKS;
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
    @SuppressWarnings(PMD_SUPPRESS)
    public String deleteTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            taskService.deleteTask(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "タスクを削除しました");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, TASK_NOT_FOUND);
        }

        return REDIRECT_TASKS;
    }

    /**
     * Toggles task completion status.
     * Uses POST-Redirect-GET pattern.
     *
     * @param id the task ID
     * @param redirectAttributes the redirect attributes
     * @return the redirect URL
     */
    @PostMapping("/{id}/toggle")
    @SuppressWarnings(PMD_SUPPRESS)
    public String toggleTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            taskService.toggleTaskCompletion(id);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, TASK_NOT_FOUND);
        }

        return REDIRECT_TASKS;
    }

    /**
     * Displays the task edit form.
     *
     * @param id the task ID
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the view name or redirect URL
     */
    @GetMapping("/{id}/edit")
    @SuppressWarnings(PMD_SUPPRESS)
    public String getEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Task task = taskService.getTaskById(id);
            model.addAttribute("task", task);
            TaskForm taskForm = new TaskForm();
            taskForm.setTitle(task.getTitle());
            model.addAttribute("taskForm", taskForm);
            return "tasks/edit";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, TASK_NOT_FOUND);
            return REDIRECT_TASKS;
        }
    }

    /**
     * Updates a task.
     * Uses POST-Redirect-GET pattern to prevent duplicate submissions.
     *
     * @param id the task ID
     * @param taskForm the task form
     * @param bindingResult the binding result
     * @param model the model
     * @param redirectAttributes the redirect attributes
     * @return the redirect URL or view name
     */
    @PostMapping("/{id}")
    @SuppressWarnings(PMD_SUPPRESS)
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Task task = taskService.getTaskById(id);
            model.addAttribute("task", task);
            return "tasks/edit";
        }

        try {
            taskService.updateTask(id, taskForm.getTitle());
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "タスクを更新しました");
            return REDIRECT_TASKS;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, TASK_NOT_FOUND);
            return REDIRECT_TASKS;
        }
    }
}
