package com.projectmanagement.controller;

import com.projectmanagement.model.ProjectAssignment;
import com.projectmanagement.service.ProjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/project-assignments")
public class ProjectAssignmentController {

    @Autowired
    private ProjectAssignmentService projectAssignmentService;

    // Assign a user to a project
    @PostMapping
    public ProjectAssignment assignUserToProject(@RequestBody ProjectAssignment assignment) {
        return projectAssignmentService.assignUserToProject(assignment);
    }

    // Get all assignments for a specific project
    @GetMapping("/project/{projectId}")
    public List<ProjectAssignment> getAssignmentsByProject(@PathVariable int projectId) {
        return projectAssignmentService.getAssignmentsByProject(projectId);
    }

    // Get all assignments for a specific user
    @GetMapping("/user/{userId}")
    public List<ProjectAssignment> getAssignmentsByUser(@PathVariable int userId) {
        return projectAssignmentService.getAssignmentsByUser(userId);
    }
}
