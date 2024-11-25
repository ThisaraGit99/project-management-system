package com.projectmanagement.repository;

import com.projectmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;  // Import Optional

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Modify this method to return Optional<User> instead of User
    Optional<User> findByEmail(String email);  // This will return Optional<User>

    boolean existsByEmail(String email);

}
