package com.jaoow.protalent.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificationDTO {

    private Long id;

    private String certificationName;

    private String issuingOrganization;

    private LocalDate issueDate;
}
