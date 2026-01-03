package com.example.auth_service.ui;


import com.example.auth_service.core.User;
import com.example.auth_service.core.UserService;
import com.example.auth_service.enums.HospitalRole;
import com.example.auth_service.ui.dto.PatientDTO;
import com.example.auth_service.ui.dto.RegistrationDTO;
import com.example.auth_service.ui.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        // 1. Hämta grundinfo från Token
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        if (name == null) name = jwt.getClaimAsString("preferred_username");

        // 2. Avgör vilken roll användaren har baserat på Keycloak-token
        HospitalRole tokenRole = determineRoleFromToken(jwt);

        // 3. JIT Provisioning & Synkronisering
        // Vi måste använda finalvariabler inuti lambdas/streams
        String finalName = name;
        HospitalRole finalRole = tokenRole;

        User user = userService.findByEmail(email)
                .map(existingUser -> {
                    // SCENARIO: Användaren finns redan.
                    // Har rollen ändrats i Keycloak sen sist? Då uppdaterar vi databasen!
                    if (existingUser.getRole() != finalRole) {
                        System.out.println("Syncing role for " + email + ": " + existingUser.getRole() + " -> " + finalRole);
                        existingUser.setRole(finalRole);
                        return userService.saveUser(existingUser); // OBS: Se till att denna metod finns i UserService
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    // SCENARIO: Användaren finns INTE (första inloggningen).
                    System.out.println("Creating new JIT user from Keycloak: " + email + " with role " + finalRole);
                    return userService.register(
                            email,
                            finalName != null ? finalName : "Unknown Name",
                            "Ej angivet", // Personnummer finns ej i token
                            "Keycloak_manage",
                            "Ej angivet", // Adress finns ej i token
                            "Ej angivet", // Telefonnummer finns ej i token
                            finalRole
                    );
                });

        return new UserDTO(
                user.getEmail(),
                user.getFullName(),
                user.getPassword(),
                user.getPersonalNumber(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    // Hjälpmetod för att extrahera roller
    private HospitalRole determineRoleFromToken(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");

            // Gör om alla till uppercase för säkerhets skull
            List<String> upperRoles = roles.stream().map(String::toUpperCase).toList();

            if (upperRoles.contains("DOCTOR")) return HospitalRole.DOCTOR;
            if (upperRoles.contains("NURSE")) return HospitalRole.NURSE;
        }
        // Default om ingen specifik roll hittas (t.ex. för vanliga användare)
        return HospitalRole.PATIENT;
    }

    // --- Övriga endpoints (Register behövs inte längre för frontend, men kan vara kvar för interna test) ---

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegistrationDTO dto) {
        User user = userService.register(
                dto.email(),
                dto.fullName(),
                dto.password(),
                dto.personalNumber(),
                dto.address(),
                dto.phoneNumber(),
                dto.role()
        );
        return new UserDTO(user.getEmail(), user.getFullName(), user.getPersonalNumber(),user.getPassword(), user.getAddress(), user.getPhoneNumber(), user.getRole());
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