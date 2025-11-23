package com.example.auth_service.core;

import com.example.auth_service.db.UserRepository;
import com.example.auth_service.enums.HospitalRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_shouldEncodePassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFullName("test");
        user.setPassword("password");
        user.setRole(HospitalRole.PATIENT);

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register(user.getEmail(), user.getPassword(), user.getFullName(),user.getRole());

        assertNotNull(saved);
        assertNotEquals("password", saved.getPassword()); // password should be encoded
        assertTrue(passwordEncoder.matches("password", saved.getPassword()));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void authenticate_shouldReturnUser_whenPasswordMatches() {
        String rawPassword = "secret";
        User user = new User("test@example.com", passwordEncoder.encode(rawPassword), "Test User", HospitalRole.PATIENT);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User authUser = userService.authenticate("test@example.com", rawPassword);
        assertNotNull(authUser);
        assertEquals("test@example.com", authUser.getEmail());
    }

    @Test
    void authenticate_shouldThrow_whenPasswordDoesNotMatch() {
        String rawPassword = "secret";
        User user = new User("test@example.com", passwordEncoder.encode(rawPassword), "Test User", HospitalRole.PATIENT);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.authenticate("test@example.com", "wrong"));
    }
}
