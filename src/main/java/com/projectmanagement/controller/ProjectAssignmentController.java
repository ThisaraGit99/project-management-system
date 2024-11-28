package com.projectmanagement.controller;

import com.projectmanagement.model.ProjectAssignment;
import com.projectmanagement.service.ProjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/admin/project-assignments")
public class ProjectAssignmentController {

    @Autowired
    private ProjectAssignmentService projectAssignmentService;

    // Assign a user to a project (only admins)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ProjectAssignment assignUserToProject(@RequestBody ProjectAssignment assignment) {
        return projectAssignmentService.assignUserToProject(assignment);
    }

    // Get all assignments for a specific project (accessible by users with appropriate roles)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/project/{projectId}")
    public List<ProjectAssignment> getAssignmentsByProject(@PathVariable int projectId) {
        return projectAssignmentService.getAssignmentsByProject(projectId);
    }

    // Get all assignments for a specific user (accessible by users with appropriate roles)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/user/{userId}")
    public List<ProjectAssignment> getAssignmentsByUser(@PathVariable int userId) {
        return projectAssignmentService.getAssignmentsByUser(userId);
    }
}
