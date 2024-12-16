package com.jaoow.protalent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificationDTO {

    private Long id;

    @NotBlank
    private String certificationName;

    @NotBlank
    private String issuingOrganization;

    private LocalDate issueDate;
}
