package com.example.auth_service.ui.dto;

import com.example.auth_service.enums.HospitalRole;

public record RegistrationDTO(String email,
                              String fullName,
                              String password,
                              String personalNumber,
                              String address,
                              String phoneNumber,
                              HospitalRole role) {

}
