package com.jaoow.protalent.specification;

import com.jaoow.protalent.dto.EmployeeFilterDTO;
import com.jaoow.protalent.model.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

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
}
