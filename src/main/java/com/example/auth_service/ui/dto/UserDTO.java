package com.example.auth_service.ui.dto;

import com.example.auth_service.enums.HospitalRole;

import java.util.UUID;

public record UserDTO(
                      String email,
                     // String password,
                      String fullName,
                      String personalNumber,
                      String address,
                      String phoneNumber,
                      HospitalRole role) {
}
