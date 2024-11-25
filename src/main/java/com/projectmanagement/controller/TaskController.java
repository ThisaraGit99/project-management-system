package com.projectmanagement.controller;

import com.projectmanagement.model.Task;
import com.projectmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Get all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Create a new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // Set createdAt field to current date/time
        task.setCreatedAt(new java.util.Date());
        return taskService.createTask(task);  // Delegate to service
    }

    // Get task by ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
        // Handle Optional<Task> returned by taskService.getTaskById(id)
        return taskService.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
    }

    // Update a task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task task) {
        return taskService.updateTask(id, task); // Delegate to service
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
    }
}
