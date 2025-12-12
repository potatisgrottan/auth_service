package com.example.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Stäng av CSRF för API:er
                .authorizeHttpRequests(auth -> auth
                        // Tillåt registrering utan inloggning (om din frontend anropar denna direkt)
                        .requestMatchers("/api/auth/register").permitAll()
                        // Alla andra anrop kräver en giltig token
                        .anyRequest().authenticated()
                )
                // Detta aktiverar validering av JWT-tokens från Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}