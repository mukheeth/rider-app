package com.ridehailing.backend.controller;

import com.ridehailing.backend.model.Role;
import com.ridehailing.backend.service.AuthenticationService;
import com.ridehailing.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    public AuthController(JwtUtil jwtUtil, AuthenticationService authenticationService) {
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register/rider")
    public ResponseEntity<Map<String, Object>> registerRider(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (request.get("email") == null || request.get("password") == null ||
                request.get("firstName") == null || request.get("lastName") == null ||
                request.get("phoneNumber") == null) {
                response.put("error", "Missing required fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Register rider using AuthenticationService
            var user = authenticationService.registerRider(
                request.get("email"),
                request.get("password"),
                request.get("phoneNumber"),
                request.get("firstName"),
                request.get("lastName")
            );

            response.put("userId", user.getUserId().toString());
            response.put("email", user.getEmail());
            response.put("message", "Registration successful");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "Registration failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register/driver")
    public ResponseEntity<Map<String, Object>> registerDriver(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (request.get("email") == null || request.get("password") == null ||
                request.get("firstName") == null || request.get("lastName") == null ||
                request.get("phoneNumber") == null || request.get("licenseNumber") == null ||
                request.get("vehicleModel") == null || request.get("vehiclePlate") == null) {
                response.put("error", "Missing required fields");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Register driver using AuthenticationService
            var user = authenticationService.registerDriver(
                request.get("email"),
                request.get("password"),
                request.get("phoneNumber"),
                request.get("firstName"),
                request.get("lastName"),
                request.get("licenseNumber"),
                request.get("vehicleModel"),
                request.get("vehiclePlate")
            );

            response.put("userId", user.getUserId().toString());
            response.put("email", user.getEmail());
            response.put("message", "Registration successful. Awaiting verification.");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "Registration failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (request.get("email") == null || request.get("password") == null) {
                response.put("error", "Email and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Authenticate user
            var user = authenticationService.authenticate(
                request.get("email"),
                request.get("password")
            );

            // Get user role
            Role userRole = authenticationService.getRoleEnum(user);
            
            // Generate JWT tokens
            String accessToken = jwtUtil.generateToken(user.getUserId().toString(), userRole);
            String refreshToken = jwtUtil.generateToken(user.getUserId().toString() + "_refresh", userRole);
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId().toString());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", userRole.name());
            
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", 3600);
            response.put("user", userInfo);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", "Invalid credentials");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (IllegalStateException e) {
            response.put("error", "Account issue");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            response.put("error", "Login failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        if (request.get("refreshToken") == null) {
            response.put("error", "Refresh token is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // TODO: Implement token refresh logic
        response.put("error", "Token refresh not yet implemented");
        
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        
        // TODO: Implement logout logic
        response.put("message", "Logout successful");
        
        return ResponseEntity.ok(response);
    }
}

