package com.projectmanagement.service;

import com.projectmanagement.exception.CustomException;
import com.projectmanagement.model.Project;
import com.projectmanagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get all projects by status (e.g., "In Progress", "Completed")
    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    // Get projects by the creator (user ID)
    public List<Project> getProjectsByCreator(int createdBy) {
        return projectRepository.findByCreatedBy(createdBy);
    }

    // Create a new project
    public Project createProject(Project project) {
        // You can add custom validation here if needed (e.g., check if project with the same name already exists)
        return projectRepository.save(project);
    }

    // Get project by ID
    public Optional<Project> getProjectById(int id) {
        return projectRepository.findById(id);
    }

    // Update a project
    public Project updateProject(int id, Project updatedProject) {
        // Check if the project exists
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
                new CustomException("Project not found with ID: " + id));

        // Update the existing project fields (but preserve createdAt)
        existingProject.setProjectName(updatedProject.getProjectName());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setCreatedBy(updatedProject.getCreatedBy());
        existingProject.setStatus(updatedProject.getStatus());  // Add status update



        // `createdAt` will stay as is, it is not updated.

        // Save and return the updated project
        return projectRepository.save(existingProject);
    }

    // Delete a project
    public void deleteProject(int id) {
        // Before deletion, you could check if the project has any tasks or users associated (if applicable)
        projectRepository.deleteById(id);
    }

}
