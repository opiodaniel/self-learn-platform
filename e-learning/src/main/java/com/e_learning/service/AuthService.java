package com.e_learning.service;

import com.e_learning.model.Role;
import com.e_learning.model.User;
import com.e_learning.repository.UserRepository;
import com.e_learning.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ResponseService responseService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, ResponseService responseService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.responseService = responseService;
    }

    public ResponseEntity<Map<String, Object>> register(User userDetail) {
        validateNewUser(userDetail.getUsername(), userDetail.getEmail());

        User user = new User();
        user.setFirstName(userDetail.getFirstName());
        user.setLastName(userDetail.getLastName());
        user.setUsername(userDetail.getUsername());
        user.setEmail(userDetail.getEmail());
        user.setRole(Role.USER);
        user.setActive(1);
        user.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        userRepository.save(user);

        Map<String, String> tokenMap = Map.of("success", "User registered successfully");
        return responseService.createSuccessResponse(201, tokenMap, HttpStatus.OK);
    }

    /**
     * Validate if username or email already exists.
     */
    private void validateNewUser(String username, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }

    public ResponseEntity<Map<String, Object>> login(User userDetail) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetail.getUsername(),
                            userDetail.getPassword()
                    )
            );

            var user = userRepository.findByUsername(userDetail.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Username or Password."));

            var jwt = jwtUtil.generateToken(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", 200);
            response.put("role", user.getRole().name());
            response.put("result", Map.of("token", jwt));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            return responseService.createErrorResponse(400,
                    Map.of("authentication", "Invalid username or password"),
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            return responseService.createErrorResponse(400,
                    Map.of("authentication", ex.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }



}