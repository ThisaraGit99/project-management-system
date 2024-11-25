package com.projectmanagement.repository;

import com.projectmanagement.model.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Integer> {

    // Find all assignments by project ID
    List<ProjectAssignment> findByProjectId(int projectId);

    // Find all assignments by user ID
    List<ProjectAssignment> findByUserId(int userId);

    // Check if a user is already assigned to a project
    boolean existsByProjectIdAndUserId(int projectId, int userId);
}
