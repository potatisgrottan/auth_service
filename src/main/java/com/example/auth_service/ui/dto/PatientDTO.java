package com.example.auth_service.ui.dto;

import com.example.auth_service.enums.HospitalRole;

public record PatientDTO(
        String id,
        String email,
        String fullName,
        HospitalRole role,
        String personalNumber,
        String address,
        String phoneNumber
) {}
