package com.projectmanagement.controller;

import com.projectmanagement.exception.CustomException;
import com.projectmanagement.model.AuthRequest;
import com.projectmanagement.model.User;
import com.projectmanagement.security.JwtUtil;
import com.projectmanagement.service.CustomUserDetailsService;
import com.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Test endpoint (for testing purposes)
    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working";
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Validate if user with the same email exists
            if (userService.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
            }

            // Register user by saving it in the database
            userService.createUser(user);  // UserService handles password encoding

            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging purposes (optional)
            e.printStackTrace();
            return new ResponseEntity<>("Error registering user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login endpoint that returns a JWT token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Load user details and generate JWT token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    // Get all users (secured)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Create a new user (secured)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Get user by ID (secured)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // Update a user (secured)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Endpoint for deleting a user (secured for admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            // Get the authenticated user (admin)
            UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Call the service to delete the user
            userService.deleteUser(id);

            // Return success response
            return ResponseEntity.ok("User deleted successfully.");

        } catch (CustomException e) {
            // Return error response if user deletion fails (e.g., user not found or other error)
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
