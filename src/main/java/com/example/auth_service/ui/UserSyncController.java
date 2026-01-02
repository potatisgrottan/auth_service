package com.example.auth_service.ui; // Ändra till ditt paketnamn

import com.example.auth_service.core.User;
import com.example.auth_service.db.UserRepository;
import com.example.auth_service.enums.HospitalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth") // Eller var du vill ha den
public class UserSyncController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sync")
    public void syncUser(@AuthenticationPrincipal Jwt principal) {
        // Hämta data från Keycloak-token
        String email = principal.getClaimAsString("email");
        String name = principal.getClaimAsString("name");

        // Hämta rollen
        HospitalRole role = HospitalRole.PATIENT; // Default
        Map<String, Object> realmAccess = principal.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles.contains("DOCTOR")) role = HospitalRole.DOCTOR;
            else if (roles.contains("NURSE")) role = HospitalRole.NURSE;
        }

        // Kolla din databas (auth_db)
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // Uppdatera om något ändrats (t.ex. fått ny roll i Keycloak)
            User user = existingUser.get();
            if (!user.getRole().equals(role)) {
                user.setRole(role);
                userRepository.save(user);
                System.out.println("Uppdaterade användare i AuthDB: " + email);
            }
        } else {
            // Skapa ny om den saknas
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name != null ? name : email);

            newUser.setRole(role);
            userRepository.save(newUser);
            System.out.println("Synkade in ny användare till AuthDB: " + email);
        }
    }
}