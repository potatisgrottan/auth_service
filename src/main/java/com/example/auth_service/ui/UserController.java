package com.example.auth_service.ui;


import com.example.auth_service.core.User;
import com.example.auth_service.core.UserService;
import com.example.auth_service.enums.HospitalRole;
import com.example.auth_service.ui.dto.LoginDTO;
import com.example.auth_service.ui.dto.PatientDTO;
import com.example.auth_service.ui.dto.RegistrationDTO;
import com.example.auth_service.ui.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        // 1. Hämta email direkt från Keycloak-token (claims)
        // "email" eller "preferred_username" beroende på din Keycloak-config
        String email = jwt.getClaimAsString("email");

        // 2. Hämta resten av profil-datan från din databas
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User profile not found for: " + email));

        // 3. Returnera DTO (Utan lösenord!)
        return new UserDTO(
                user.getEmail(),
                null, // Inget lösenord ska skickas tillbaka
                user.getFullName(),
                user.getPersonalNumber(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegistrationDTO dto) {
        userService.register(
                dto.email(),
                dto.fullName(),
                dto.personalNumber(),
                dto.address(),
                dto.phoneNumber(),
                dto.role()
        );


        return new UserDTO(dto.email(),
                dto.fullName(), dto.personalNumber(), dto.address(), dto.phoneNumber(), dto.role());
    }


    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<PatientDTO>> getUsersByRole(@PathVariable String role) {
        HospitalRole hospitalRole;
        try {
            hospitalRole = HospitalRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<User> users = userService.findByRole(hospitalRole);
        List<PatientDTO> dtos = users.stream()
                .map(u -> new PatientDTO(
                        u.getId(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole(),
                        u.getPersonalNumber(),
                        u.getAddress(),
                        u.getPhoneNumber()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


}

