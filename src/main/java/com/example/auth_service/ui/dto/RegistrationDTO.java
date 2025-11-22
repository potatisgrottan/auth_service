package com.example.auth_service.ui.dto;

import java.util.Date;
import java.util.UUID;

public class RegistrationDTO {
    private String email;
    private String password;
    private String role;

    private String name;
    private String address;
    private Date dateOfBirth;
    private String personalNumber;
    private String phoneNumber;

    private String practitionerName;
    private String practitionerPhoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public void setPractitionerName(String practitionerName) {
        this.practitionerName = practitionerName;
    }

    public String getPractitionerPhoneNumber() {
        return practitionerPhoneNumber;
    }

    public void setPractitionerPhoneNumber(String practitionerPhoneNumber) {
        this.practitionerPhoneNumber = practitionerPhoneNumber;
    }
}
