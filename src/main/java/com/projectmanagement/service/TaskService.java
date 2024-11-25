package com.projectmanagement.service;

import com.projectmanagement.model.Project;
import com.projectmanagement.model.Task;
import com.projectmanagement.model.User;
import com.projectmanagement.repository.TaskRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create a new task
    public Task createTask(Task task) {
        // Set the project and user based on their IDs
        Project project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        User user = userRepository.findById(task.getAssignedTo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        task.setProject(project);
        task.setAssignedTo(user);
        task.setCreatedAt(new java.util.Date());  // Set the createdAt to current time

        return taskRepository.save(task);
    }

    // Get task by ID
    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    // Update a task
    public Task updateTask(int id, Task task) {
        // Ensure the task exists
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Task not found with ID: " + id));

        // Set the ID for the task and update other fields
        task.setId(id);
        existingTask.setTaskName(task.getTaskName());
        existingTask.setStatus(task.getStatus());
        existingTask.setProject(task.getProject());  // Set the project based on the ID
        existingTask.setAssignedTo(task.getAssignedTo());  // Set the assigned user based on the ID

        return taskRepository.save(existingTask);
    }

    // Delete a task
    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }
}
