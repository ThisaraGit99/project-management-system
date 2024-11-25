package com.projectmanagement.service;

import com.projectmanagement.model.ProjectAssignment;
import com.projectmanagement.repository.ProjectAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectAssignmentService {

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    // Assign a user to a project
    public ProjectAssignment assignUserToProject(ProjectAssignment assignment) {
        if (projectAssignmentRepository.existsByProjectIdAndUserId(assignment.getProjectId(), assignment.getUserId())) {
            throw new RuntimeException("User is already assigned to this project.");
        }
        return projectAssignmentRepository.save(assignment);
    }

    // Get all assignments for a specific project
    public List<ProjectAssignment> getAssignmentsByProject(int projectId) {
        return projectAssignmentRepository.findByProjectId(projectId);
    }

    // Get all assignments for a specific user
    public List<ProjectAssignment> getAssignmentsByUser(int userId) {
        return projectAssignmentRepository.findByUserId(userId);
    }
}
