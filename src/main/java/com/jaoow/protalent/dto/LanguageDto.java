package com.jaoow.protalent.dto;

import com.jaoow.protalent.enums.LanguageProficiencyLevel;
import com.jaoow.protalent.model.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link Language}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDto implements Serializable {

    @NotBlank(message = "Language name is required")
    private String languageName;

    @NotNull(message = "Proficiency level is required")
    private LanguageProficiencyLevel proficiencyLevel;
}