package com.ridehailing.backend.controller;

import com.ridehailing.backend.model.Role;
import com.ridehailing.backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void riderEndpoint_WithRiderToken_ShouldReturn200() throws Exception {
        String userId = "rider-user-123";
        String token = jwtUtil.generateToken(userId, Role.RIDER);

        mockMvc.perform(get("/api/v1/rider/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rider profile endpoint"))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.role").value("RIDER"));
    }

    @Test
    void riderEndpoint_WithDriverToken_ShouldReturn403() throws Exception {
        String userId = "driver-user-123";
        String token = jwtUtil.generateToken(userId, Role.DRIVER);

        mockMvc.perform(get("/api/v1/rider/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void riderEndpoint_WithAdminToken_ShouldReturn403() throws Exception {
        String userId = "admin-user-123";
        String token = jwtUtil.generateToken(userId, Role.ADMIN);

        mockMvc.perform(get("/api/v1/rider/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void driverEndpoint_WithDriverToken_ShouldReturn200() throws Exception {
        String userId = "driver-user-123";
        String token = jwtUtil.generateToken(userId, Role.DRIVER);

        mockMvc.perform(get("/api/v1/driver/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Driver profile endpoint"))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.role").value("DRIVER"));
    }

    @Test
    void driverEndpoint_WithRiderToken_ShouldReturn403() throws Exception {
        String userId = "rider-user-123";
        String token = jwtUtil.generateToken(userId, Role.RIDER);

        mockMvc.perform(get("/api/v1/driver/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void driverEndpoint_WithAdminToken_ShouldReturn403() throws Exception {
        String userId = "admin-user-123";
        String token = jwtUtil.generateToken(userId, Role.ADMIN);

        mockMvc.perform(get("/api/v1/driver/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_WithAdminToken_ShouldReturn200() throws Exception {
        String userId = "admin-user-123";
        String token = jwtUtil.generateToken(userId, Role.ADMIN);

        mockMvc.perform(get("/api/v1/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin dashboard endpoint"))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void adminEndpoint_WithRiderToken_ShouldReturn403() throws Exception {
        String userId = "rider-user-123";
        String token = jwtUtil.generateToken(userId, Role.RIDER);

        mockMvc.perform(get("/api/v1/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_WithDriverToken_ShouldReturn403() throws Exception {
        String userId = "driver-user-123";
        String token = jwtUtil.generateToken(userId, Role.DRIVER);

        mockMvc.perform(get("/api/v1/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void riderEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/rider/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void driverEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/driver/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/admin/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}

