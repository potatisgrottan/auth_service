package com.example.auth_service.ui;



import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(@RequestHeader("Authorization") String authHeader) {

        if (!authHeader.startsWith("Basic ")) {
            throw new RuntimeException("Invalid auth header");
        }

        String base64 = authHeader.substring(6);
        String decoded = new String(java.util.Base64.getDecoder().decode(base64));

        String[] parts = decoded.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid credentials format");
        }

        String email = parts[0];
        String password = parts[1];

        User user = userService.authenticate(email, password);
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().toString()
        );
    }

    public record UserResponse(
            java.util.UUID id,
            String email,
            String role
    ) {}
}

