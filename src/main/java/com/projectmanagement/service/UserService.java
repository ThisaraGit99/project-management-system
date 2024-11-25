package com.projectmanagement.service;

import com.projectmanagement.exception.CustomException;
import com.projectmanagement.model.User;
import com.projectmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constants for roles (optional but helps avoid errors due to hardcoded strings)
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Create a new user
    public User createUser(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new CustomException("User already exists with ID: " + user.getId());
        }

        // Set the default role to 'ROLE_USER' if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(ROLE_USER);  // Default to 'ROLE_USER' role
        }

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get user by ID
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomException("User not found with ID: " + id));
    }

    // Update a user
    public User updateUser(int id, User user) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found with ID: " + id);
        }

        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new CustomException("User not found with ID: " + id));

        // Ensure the ID is preserved and don't update createdAt
        user.setId(id);  // Ensure the ID is preserved

        // Nullify createdAt field to avoid overwriting it during update
        user.setCreatedAt(existingUser.getCreatedAt());

        // If the role is not provided, keep the existing role
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(existingUser.getRole());
        }

        // Encode password if it is being updated
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    // Delete a user
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Method to register a new user with encoded password
    public User registerUser(User user) {
        // Set the default role to 'ROLE_USER' if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(ROLE_USER);  // Default to 'ROLE_USER' role
        }

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Check if a user exists by email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Fetch user ID by email (used in ProjectController)
    public int getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
        return user.getId();
    }
}
