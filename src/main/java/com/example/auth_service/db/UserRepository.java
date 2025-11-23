package com.example.auth_service.db;


import com.example.auth_service.enums.HospitalRole;
import com.example.auth_service.core.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(HospitalRole role);
    List<User> findByRoleIn(List<String> roles);
}

