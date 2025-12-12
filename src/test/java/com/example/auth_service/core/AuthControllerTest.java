package com.example.auth_service.core;

import com.example.auth_service.enums.HospitalRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void getCurrentUser_shouldReturnUser() throws Exception {
        // arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User(
                "test@example.com",
                encoder.encode("secret"),
                "Test User",
                "19900101-1234",   // personalNumber
                "Testgatan 1",     // address
                "0701234567",      // phoneNumber
                HospitalRole.PATIENT
        );

        // Viktigt: du måste använda samma register-metod som din controller!
        userService.register(
                user.getEmail(),
                "secret",             // plaintext password
                user.getFullName(),
                user.getPersonalNumber(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getRole()
        );

        String authHeader = "Basic " + Base64.getEncoder().encodeToString(
                "test@example.com:secret".getBytes()
        );

        // act & assert
        mockMvc.perform(get("/api/auth/me").header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }
}*/
