package com.jaoow.protalent.specification;

import com.jaoow.protalent.dto.EmployeeFilterDTO;
import com.jaoow.protalent.enums.LanguageProficiencyLevel;
import com.jaoow.protalent.model.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class EmployeeSpecification {

    public static Specification<Employee> filter(EmployeeFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (query == null) {
                return predicate;
            }

            if (filterDTO.getName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDTO.getName().toLowerCase() + "%"));
            }

            if (filterDTO.getEmail() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("email"), filterDTO.getEmail()));
            }

            if (filterDTO.getMinExperienceYears() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("experienceYears"), filterDTO.getMinExperienceYears()));
            }

            if (filterDTO.getTechnicalSkill() != null) {
                query.distinct(true);
                predicate = criteriaBuilder.and(predicate,
                        applyLikeForMultipleValues(filterDTO.getTechnicalSkill(), root, criteriaBuilder, "technicalSkills", "skillName"));
            }

            if (filterDTO.getCertification() != null) {
                query.distinct(true);
                predicate = criteriaBuilder.and(predicate,
                        applyLikeForMultipleValues(filterDTO.getCertification(), root, criteriaBuilder, "certifications", "certificationName"));
            }

            if (filterDTO.getLanguage() != null) {
                query.distinct(true);
                predicate = criteriaBuilder.and(predicate,
                        applyLanguageFilter(filterDTO.getLanguage(), root, criteriaBuilder));
            }

            return predicate;
        };
    }

    private static Predicate applyLikeForMultipleValues(String value, Root<Employee> root,
                                                        CriteriaBuilder criteriaBuilder, String joinField, String fieldName) {
        Predicate predicate = criteriaBuilder.conjunction();
        String[] values = value.split(",");

        for (String val : values) {
            val = val.trim();
            if (!val.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.join(joinField).get(fieldName)), "%" + val.toLowerCase() + "%"));
            }
        }
        return predicate;
    }

    private static Predicate applyLanguageFilter(String value, Root<Employee> root, CriteriaBuilder criteriaBuilder) {
        String[] languageData = value.split("-");
        String languageName = languageData[0].trim();

        Predicate nameCriteria = criteriaBuilder.equal(criteriaBuilder.lower(root.join("languages").get("languageName")), languageName.toLowerCase());
        if (languageData.length < 2) {
            return nameCriteria;
        }

        String proficiencyLevel = languageData[1].trim();

        LanguageProficiencyLevel level = LanguageProficiencyLevel.getLanguageProficiencyLevel(proficiencyLevel.toUpperCase());
        if (level == null) {
            return nameCriteria;
        }

        int levelOrdinal = level.ordinal();

        return criteriaBuilder.and(
                nameCriteria,
                criteriaBuilder.greaterThanOrEqualTo(root.join("languages").get("proficiencyLevel"), levelOrdinal)
        );
    }
}
