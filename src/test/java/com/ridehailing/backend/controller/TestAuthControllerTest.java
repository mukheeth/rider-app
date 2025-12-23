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
class TestAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void secureEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/test/secure"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void secureEndpoint_WithValidToken_ShouldReturn200() throws Exception {
        String userId = "test-user-123";
        String token = jwtUtil.generateToken(userId, Role.RIDER);

        mockMvc.perform(get("/api/v1/test/secure")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secure endpoint"))
                .andExpect(jsonPath("$.authenticatedUser").value(userId));
    }

    @Test
    void secureEndpoint_WithInvalidToken_ShouldReturn401() throws Exception {
        String invalidToken = "invalid.jwt.token";

        mockMvc.perform(get("/api/v1/test/secure")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void secureEndpoint_WithMalformedToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/test/secure")
                        .header("Authorization", "Bearer not.a.valid.token.format"))
                .andExpect(status().isUnauthorized());
    }
}

