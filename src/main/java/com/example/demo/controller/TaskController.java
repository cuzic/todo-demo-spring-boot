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
 *
 * <p>This controller handles all HTTP requests related to task management,
 * including CRUD operations (Create, Read, Update, Delete) and filtering.</p>
 *
 * <p>Endpoints:</p>
 * <ul>
 *   <li>GET /tasks - Display task list with optional filtering</li>
 *   <li>POST /tasks - Create a new task</li>
 *   <li>GET /tasks/{id}/edit - Display task edit form</li>
 *   <li>POST /tasks/{id} - Update an existing task</li>
 *   <li>POST /tasks/{id}/delete - Delete a task</li>
 *   <li>POST /tasks/{id}/toggle - Toggle task completion status</li>
 * </ul>
 *
 * <p>All POST endpoints follow the Post-Redirect-Get (PRG) pattern to prevent
 * duplicate form submissions and provide a better user experience.</p>
 *
 * <p>Filtering options:</p>
 * <ul>
 *   <li>?filter=active - Show only incomplete tasks</li>
 *   <li>?filter=completed - Show only completed tasks</li>
 *   <li>No filter - Show all tasks</li>
 * </ul>
 *
 * @see TaskService
 * @see TaskForm
 * @see Task
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    /** Redirect URL for task list page. */
    private static final String REDIRECT_TASKS = "redirect:/tasks";

    /** Model attribute key for error messages. */
    private static final String ERROR_MESSAGE = "errorMessage";

    /** Model attribute key for success messages. */
    private static final String SUCCESS_MESSAGE = "successMessage";

    /** Error message text for task not found scenarios. */
    private static final String TASK_NOT_FOUND = "タスクが見つかりませんでした";

    /** PMD suppression constant for generic exception catching. */
    private static final String PMD_SUPPRESS = "PMD.AvoidCatchingGenericException";

    /** Filter value for active (incomplete) tasks. */
    private static final String FILTER_ACTIVE = "active";

    /** Filter value for completed tasks. */
    private static final String FILTER_COMPLETED = "completed";

    /**
     * The task service for business logic operations.
     * Injected via constructor dependency injection.
     */
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
