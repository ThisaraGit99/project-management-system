package com.projectmanagement.repository;

import com.projectmanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    // Find projects by the status (e.g., "In Progress", "Completed")
    List<Project> findByStatus(String status);

    // Find projects created by a specific user (using created_by field)
    List<Project> findByCreatedBy(int createdBy);

    // You can add more custom queries if needed

}
