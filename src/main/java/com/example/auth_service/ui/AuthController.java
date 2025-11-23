package com.example.auth_service.ui;

import com.example.auth_service.core.User;
import com.example.auth_service.core.UserService;
import com.example.auth_service.ui.dto.LoginDTO;
import com.example.auth_service.ui.dto.RegistrationDTO;
import com.example.auth_service.ui.dto.UserDTO;
import org.springframework.web.bind.annotation.*;


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

}

