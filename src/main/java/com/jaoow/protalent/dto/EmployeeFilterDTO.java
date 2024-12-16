package com.jaoow.protalent.dto;

import lombok.Data;

@Data
public class EmployeeFilterDTO {
    private String name;
    private String email;
    private String contact;
    private String technicalSkill;
    private String certification;
    private String language;
    private Integer minExperienceYears;
}
