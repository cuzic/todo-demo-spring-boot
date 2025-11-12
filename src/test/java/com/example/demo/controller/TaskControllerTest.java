package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for TaskController using MockMVC.
 */
@WebMvcTest(TaskController.class)
@WithMockUser
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    // ========== Task List Display Tests ==========

    @Test
    void testGetTaskList_WithTasks_DisplaysTasks() throws Exception {
        // Given
        Task task1 = new Task("タスク1");
        task1.setId(1L);
        Task task2 = new Task("タスク2");
        task2.setId(2L);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attribute("tasks", hasSize(2)));

        verify(taskService).getAllTasks();
    }

    @Test
    void testGetTaskList_EmptyList_ShowsMessage() throws Exception {
        // Given
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attribute("tasks", hasSize(0)));

        verify(taskService).getAllTasks();
    }

    @Test
    void testGetTaskList_ReturnsCorrectView() throws Exception {
        // Given
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeExists("taskForm"));

        verify(taskService).getAllTasks();
    }

    // ========== Task Creation Tests ==========

    @Test
    void testCreateTask_ValidInput_RedirectsToList() throws Exception {
        // Given
        Task task = new Task("新しいタスク");
        task.setId(1L);
        when(taskService.createTask(anyString())).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks")
                .with(csrf())
                .param("title", "新しいタスク"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("successMessage"));

        verify(taskService).createTask("新しいタスク");
    }

    @Test
    void testCreateTask_EmptyTitle_ShowsError() throws Exception {
        // When & Then
        mockMvc.perform(post("/tasks")
                .with(csrf())
                .param("title", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeHasFieldErrors("taskForm", "title"));
    }

    @Test
    void testCreateTask_TitleTooLong_ShowsError() throws Exception {
        // Given
        String longTitle = "a".repeat(256);

        // When & Then
        mockMvc.perform(post("/tasks")
                .with(csrf())
                .param("title", longTitle))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeHasFieldErrors("taskForm", "title"));
    }

    @Test
    void testCreateTask_CreatesTaskInDatabase() throws Exception {
        // Given
        String title = "テストタスク";
        Task task = new Task(title);
        task.setId(1L);
        when(taskService.createTask(title)).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks")
                .with(csrf())
                .param("title", title))
            .andExpect(status().is3xxRedirection());

        verify(taskService).createTask(title);
    }

    // ========== Task Deletion Tests ==========

    @Test
    void testDeleteTask_ValidId_RedirectsToList() throws Exception {
        // Given
        Long taskId = 1L;

        // When & Then
        mockMvc.perform(post("/tasks/{id}/delete", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("successMessage"));

        verify(taskService).deleteTask(taskId);
    }

    @Test
    void testDeleteTask_NonExistentId_ShowsError() throws Exception {
        // Given
        Long taskId = 999L;
        doThrow(new RuntimeException("Task not found")).when(taskService).deleteTask(taskId);

        // When & Then
        mockMvc.perform(post("/tasks/{id}/delete", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("errorMessage"));

        verify(taskService).deleteTask(taskId);
    }

    @Test
    void testDeleteTask_RemovesTaskFromDatabase() throws Exception {
        // Given
        Long taskId = 1L;

        // When
        mockMvc.perform(post("/tasks/{id}/delete", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection());

        // Then
        verify(taskService).deleteTask(taskId);
    }

    // ========== Task Toggle Completion Tests ==========

    @Test
    void testToggleTask_Success_RedirectsToList() throws Exception {
        // Given
        Long taskId = 1L;
        Task task = new Task("テストタスク");
        task.setId(taskId);
        task.setCompleted(false);
        when(taskService.toggleTaskCompletion(taskId)).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks/{id}/toggle", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"));

        verify(taskService).toggleTaskCompletion(taskId);
    }

    @Test
    void testToggleTask_TogglesCompletionStatus() throws Exception {
        // Given
        Long taskId = 1L;
        Task task = new Task("テストタスク");
        task.setId(taskId);
        task.setCompleted(true);
        when(taskService.toggleTaskCompletion(taskId)).thenReturn(task);

        // When
        mockMvc.perform(post("/tasks/{id}/toggle", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection());

        // Then
        verify(taskService).toggleTaskCompletion(taskId);
    }

    @Test
    void testToggleTask_NonExistentId_ShowsError() throws Exception {
        // Given
        Long taskId = 999L;
        doThrow(new RuntimeException("Task not found")).when(taskService).toggleTaskCompletion(taskId);

        // When & Then
        mockMvc.perform(post("/tasks/{id}/toggle", taskId)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("errorMessage"));

        verify(taskService).toggleTaskCompletion(taskId);
    }

    // ========== Task Edit Form Tests ==========

    @Test
    void testGetEditForm_ValidId_ReturnsEditView() throws Exception {
        // Given
        Long taskId = 1L;
        Task task = new Task("テストタスク");
        task.setId(taskId);
        when(taskService.getTaskById(taskId)).thenReturn(task);

        // When & Then
        mockMvc.perform(get("/tasks/{id}/edit", taskId))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/edit"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("taskForm"));

        verify(taskService).getTaskById(taskId);
    }

    @Test
    void testGetEditForm_NonExistentId_ShowsError() throws Exception {
        // Given
        Long taskId = 999L;
        when(taskService.getTaskById(taskId)).thenThrow(new RuntimeException("Task not found"));

        // When & Then
        mockMvc.perform(get("/tasks/{id}/edit", taskId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("errorMessage"));

        verify(taskService).getTaskById(taskId);
    }

    // ========== Task Update Tests ==========

    @Test
    void testUpdateTask_ValidInput_RedirectsToList() throws Exception {
        // Given
        Long taskId = 1L;
        String newTitle = "更新されたタスク";
        Task task = new Task(newTitle);
        task.setId(taskId);
        when(taskService.updateTask(taskId, newTitle)).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks/{id}", taskId)
                .with(csrf())
                .param("title", newTitle))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("successMessage"));

        verify(taskService).updateTask(taskId, newTitle);
    }

    @Test
    void testUpdateTask_EmptyTitle_ShowsError() throws Exception {
        // Given
        Long taskId = 1L;
        Task task = new Task("テストタスク");
        task.setId(taskId);
        when(taskService.getTaskById(taskId)).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks/{id}", taskId)
                .with(csrf())
                .param("title", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/edit"))
            .andExpect(model().attributeHasFieldErrors("taskForm", "title"));
    }

    @Test
    void testUpdateTask_TitleTooLong_ShowsError() throws Exception {
        // Given
        Long taskId = 1L;
        String longTitle = "a".repeat(256);
        Task task = new Task("テストタスク");
        task.setId(taskId);
        when(taskService.getTaskById(taskId)).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks/{id}", taskId)
                .with(csrf())
                .param("title", longTitle))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/edit"))
            .andExpect(model().attributeHasFieldErrors("taskForm", "title"));
    }

    @Test
    void testUpdateTask_NonExistentId_ShowsError() throws Exception {
        // Given
        Long taskId = 999L;
        when(taskService.updateTask(taskId, "新しいタイトル"))
            .thenThrow(new RuntimeException("Task not found"));

        // When & Then
        mockMvc.perform(post("/tasks/{id}", taskId)
                .with(csrf())
                .param("title", "新しいタイトル"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/tasks"))
            .andExpect(flash().attributeExists("errorMessage"));

        verify(taskService).updateTask(taskId, "新しいタイトル");
    }

    @Test
    void testUpdateTask_UpdatesDatabase() throws Exception {
        // Given
        Long taskId = 1L;
        String newTitle = "更新されたタスク";
        Task task = new Task(newTitle);
        task.setId(taskId);
        when(taskService.updateTask(taskId, newTitle)).thenReturn(task);

        // When
        mockMvc.perform(post("/tasks/{id}", taskId)
                .with(csrf())
                .param("title", newTitle))
            .andExpect(status().is3xxRedirection());

        // Then
        verify(taskService).updateTask(taskId, newTitle);
    }
}
