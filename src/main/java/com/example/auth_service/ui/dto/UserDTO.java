package com.example.auth_service.ui.dto;

import com.example.auth_service.enums.HospitalRole;

import java.util.UUID;

public record UserDTO(String id,
                      String email,
                      String fullName,
                      HospitalRole role) {
}
