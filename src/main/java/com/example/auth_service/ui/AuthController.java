package com.example.auth_service.ui;

import com.example.auth_service.core.User;
import com.example.auth_service.core.UserService;
import com.example.auth_service.enums.HospitalRole;
import com.example.auth_service.ui.dto.LoginDTO;
import com.example.auth_service.ui.dto.RegistrationDTO;
import com.example.auth_service.ui.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser(@RequestHeader("Authorization") String authHeader) { if (!authHeader.startsWith("Basic ")) {
        throw new RuntimeException("Invalid auth header");
    }

        String base64 = authHeader.substring(6);
        String decoded = new String(java.util.Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(":");
        if (parts.length != 2) throw new RuntimeException("Invalid credentials format");

        String email = parts[0];
        String password = parts[1];

        User user = userService.authenticate(email, password);
        return new UserDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegistrationDTO dto) {
        User user = userService.register(
                dto.email(),
                dto.password(),
                dto.fullName(),
                dto.role()
        );
        return new UserDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO dto) {
        User user = userService.authenticate(dto.email(), dto.password());
        return new UserDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        HospitalRole hospitalRole;
        try {
            hospitalRole = HospitalRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<User> users = userService.findByRole(hospitalRole);
        List<UserDTO> dtos = users.stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail(), u.getFullName(), u.getRole()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("AuthService: Received Authorization header = " + authHeader);

        if (!authHeader.startsWith("Basic ")) {
            System.out.println("AuthService: Invalid header format");
            return ResponseEntity.status(401).build();
        }

        String base64 = authHeader.substring(6);
        String decoded = new String(java.util.Base64.getDecoder().decode(base64));
        System.out.println("AuthService: Decoded = " + decoded);

        String[] parts = decoded.split(":");
        if (parts.length != 2) {
            System.out.println("AuthService: Invalid credentials format");
            return ResponseEntity.status(401).build();
        }

        String email = parts[0];
        String password = parts[1];
        System.out.println("AuthService: Authenticating email = " + email);

        try {
            User user = userService.authenticate(email, password);
            return ResponseEntity.ok(new UserDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole()));
        } catch (Exception e) {
            System.out.println("AuthService: Authentication failed = " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }


}

