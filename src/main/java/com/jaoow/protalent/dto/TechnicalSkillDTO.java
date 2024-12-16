package com.jaoow.protalent.dto;

import com.jaoow.protalent.enums.TechnicalProficiencyLevel;
import lombok.Data;

@Data
public class TechnicalSkillDTO {

    private Long id;

    private String skillName;

    private TechnicalProficiencyLevel proficiencyLevel;
}
