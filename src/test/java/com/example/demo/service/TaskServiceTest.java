package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TaskService.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testCreateTask_Success() {
        // Given
        String title = "新しいタスク";
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);
        task.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.createTask(title);

        // Then
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertFalse(result.isCompleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testGetAllTasks_ReturnsAllTasks() {
        // Given
        Task task1 = new Task("タスク1");
        Task task2 = new Task("タスク2");
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void testGetTaskById_Success() {
        // Given
        Long taskId = 1L;
        Task task = new Task("テストタスク");
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        Task result = taskService.getTaskById(taskId);

        // Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("テストタスク", result.getTitle());
        verify(taskRepository).findById(taskId);
    }

    @Test
    void testGetTaskById_NotFound_ThrowsException() {
        // Given
        Long taskId = 999L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId));
        verify(taskRepository).findById(taskId);
    }

    @Test
    void testUpdateTask_Success() {
        // Given
        Long taskId = 1L;
        String newTitle = "更新されたタスク";
        Task existingTask = new Task("古いタスク");
        existingTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Task result = taskService.updateTask(taskId, newTitle);

        // Then
        assertNotNull(result);
        assertEquals(newTitle, result.getTitle());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void testDeleteTask_Success() {
        // Given
        Long taskId = 1L;
        Task task = new Task("削除するタスク");
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteTask_NotFound_ThrowsException() {
        // Given
        Long taskId = 999L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(taskId));
        verify(taskRepository).findById(taskId);
    }

    @Test
    void testToggleTaskCompletion_FromFalseToTrue() {
        // Given
        Long taskId = 1L;
        Task task = new Task("未完了タスク");
        task.setId(taskId);
        task.setCompleted(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.toggleTaskCompletion(taskId);

        // Then
        assertNotNull(result);
        assertTrue(result.isCompleted());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
    }

    @Test
    void testToggleTaskCompletion_FromTrueToFalse() {
        // Given
        Long taskId = 1L;
        Task task = new Task("完了タスク");
        task.setId(taskId);
        task.setCompleted(true);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task result = taskService.toggleTaskCompletion(taskId);

        // Then
        assertNotNull(result);
        assertFalse(result.isCompleted());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
    }

    @Test
    void testGetActiveTasks_ReturnsOnlyIncompleteTasks() {
        // Given
        Task activeTask1 = new Task("未完了タスク1");
        activeTask1.setCompleted(false);
        Task activeTask2 = new Task("未完了タスク2");
        activeTask2.setCompleted(false);
        List<Task> activeTasks = Arrays.asList(activeTask1, activeTask2);

        when(taskRepository.findByCompletedOrderByCreatedAtDesc(false)).thenReturn(activeTasks);

        // When
        List<Task> result = taskService.getActiveTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.get(0).isCompleted());
        assertFalse(result.get(1).isCompleted());
        verify(taskRepository).findByCompletedOrderByCreatedAtDesc(false);
    }

    @Test
    void testGetCompletedTasks_ReturnsOnlyCompletedTasks() {
        // Given
        Task completedTask1 = new Task("完了タスク1");
        completedTask1.setCompleted(true);
        Task completedTask2 = new Task("完了タスク2");
        completedTask2.setCompleted(true);
        List<Task> completedTasks = Arrays.asList(completedTask1, completedTask2);

        when(taskRepository.findByCompletedOrderByCreatedAtDesc(true)).thenReturn(completedTasks);

        // When
        List<Task> result = taskService.getCompletedTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isCompleted());
        assertTrue(result.get(1).isCompleted());
        verify(taskRepository).findByCompletedOrderByCreatedAtDesc(true);
    }

    @Test
    void testGetActiveTasks_ReturnsEmptyList_WhenNoActiveTasks() {
        // Given
        List<Task> emptyList = Collections.emptyList();

        when(taskRepository.findByCompletedOrderByCreatedAtDesc(false)).thenReturn(emptyList);

        // When
        List<Task> result = taskService.getActiveTasks();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(taskRepository).findByCompletedOrderByCreatedAtDesc(false);
    }

    @Test
    void testGetCompletedTasks_ReturnsEmptyList_WhenNoCompletedTasks() {
        // Given
        List<Task> emptyList = Collections.emptyList();

        when(taskRepository.findByCompletedOrderByCreatedAtDesc(true)).thenReturn(emptyList);

        // When
        List<Task> result = taskService.getCompletedTasks();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(taskRepository).findByCompletedOrderByCreatedAtDesc(true);
    }
}
