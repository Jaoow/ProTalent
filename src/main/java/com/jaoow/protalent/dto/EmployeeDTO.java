package com.jaoow.protalent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String contact;

    private List<TechnicalSkillDTO> technicalSkills;

    private List<CertificationDTO> certifications;

    private int experienceYears;

    private String linkedinUrl;

    public EmployeeDTO() {
        this.technicalSkills = new ArrayList<>();
        this.certifications = new ArrayList<>();
    }
}
