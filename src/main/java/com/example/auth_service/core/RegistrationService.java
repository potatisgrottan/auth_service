package com.example.auth_service.core;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RegistrationService {
/* TODO FIX USER Patient practitioner dependancy
    private final UserService userService;
    private final PatientService patientService;
    private final PractitionerService practitionerService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserService userService,
                               PatientService patientService,
                               PractitionerService practitionerService,
                               PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.patientService = patientService;
        this.practitionerService = practitionerService;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegistrationDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole().toUpperCase());
        userService.save(user);

        switch (dto.getRole().toUpperCase()) {
            case "PATIENT" -> {
                Patient patient = new Patient();
                patient.setName(dto.getName());
                patient.setAddress(dto.getAddress());
                patient.setDateOfBirth(dto.getDateOfBirth());
                patient.setPersonalNumber(dto.getPersonalNumber());
                patient.setPhoneNumber(dto.getPhoneNumber());
                patient.setRole(HospitalRole.PATIENT);
                patient.setUser(user);
                patientService.save(patient);
            }
            case "DOCTOR", "NURSE" -> {
                Practitioner practitioner = new Practitioner();
                practitioner.setName(dto.getName());
                practitioner.setPhoneNumber(dto.getPhoneNumber());
                practitioner.setRole(HospitalRole.valueOf(dto.getRole().toUpperCase()));
                practitioner.setUser(user);
                practitionerService.save(practitioner);
            }
            default -> throw new IllegalArgumentException("Invalid role: " + dto.getRole());
        }
    }*/
}

