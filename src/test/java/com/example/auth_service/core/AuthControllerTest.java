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
        User user = new User("test@example.com", new BCryptPasswordEncoder().encode("secret"), "Test User", HospitalRole.PATIENT);
        userService.register(user.getEmail(), user.getPassword(), user.getFullName(), user.getRole());

        String authHeader = "Basic " + Base64.getEncoder().encodeToString("test@example.com:secret".getBytes());

        // act & assert
        mockMvc.perform(get("/api/auth/me").header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
