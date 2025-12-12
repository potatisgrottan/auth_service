package com.example.auth_service.core;


import com.example.auth_service.enums.HospitalRole;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;


@Entity
@Data // Skapar getters, setters, toString, equals, hashcode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

   /* @Column(name = "password_hash", nullable = false)
    private String password;*/

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "personal_number")
    private String personalNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalRole role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}