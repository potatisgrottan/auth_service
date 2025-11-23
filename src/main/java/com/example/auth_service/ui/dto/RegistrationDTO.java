package com.example.auth_service.ui.dto;

import com.example.auth_service.enums.HospitalRole;

public record RegistrationDTO(String email,
                              String password,
                              String fullName,
                              HospitalRole role) {

}
