package com.example.userservice;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

// ── OLD import — needs migration in Spring Boot 3 ──
import org.springframework.boot.web.server.LocalServerPort;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("GET /api/v1/users - should return 401 without auth")
    void getAllUsers_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/users - should return 200 for ADMIN")
    void getAllUsers_AsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/users - should create user")
    void createUser_Success() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("Password@123")
                .firstName("New")
                .lastName("User")
                .role(User.Role.ROLE_USER)
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/v1/users - should return 400 for invalid request")
    void createUser_ValidationFail() throws Exception {
        CreateUserRequest invalid = CreateUserRequest.builder()
                .username("x")          // too short
                .email("not-an-email")  // invalid
                .password("short")      // too short
                .firstName("")          // blank
                .lastName("Doe")
                .role(User.Role.ROLE_USER)
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /api/v1/users - should return 403 for non-ADMIN")
    void createUser_Forbidden() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser2")
                .email("test2@example.com")
                .password("Password@123")
                .firstName("Test")
                .lastName("User")
                .role(User.Role.ROLE_USER)
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/users/stats - should return stats")
    void getUserStats() throws Exception {
        mockMvc.perform(get("/api/v1/users/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").exists());
    }

    @Test
    @DisplayName("GET /actuator/health - should be public")
    void actuatorHealth_Public() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
