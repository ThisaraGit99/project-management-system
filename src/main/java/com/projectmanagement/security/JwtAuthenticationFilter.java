package com.projectmanagement.security;


import com.projectmanagement.service.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component // This marks the class as a Spring-managed bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    // Constructor injection for better testing and maintenance
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Override doFilterInternal from OncePerRequestFilter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the request contains the "Authorization" header and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // Extract token
            String username = jwtUtil.extractUsername(token);  // Extract username from token

            // If the username is valid and no authentication is set yet, authenticate the user
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // Load user details

                    // Validate the token and set the authentication context
                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // Set authentication
                    } else {
                        logger.warn("Invalid JWT token for user: {}", username);  // Log invalid token
                    }
                } catch (Exception e) {
                    logger.error("Error processing JWT token", e);  // Log error
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Set unauthorized status
                    response.getWriter().write("Invalid or expired token");
                    return;  // Prevent further processing if token is invalid
                }
            }
        } else {
            logger.warn("Authorization header is missing or incorrect");  // Log missing or incorrect header
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
