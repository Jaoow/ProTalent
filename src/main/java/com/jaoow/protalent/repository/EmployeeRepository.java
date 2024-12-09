package com.jaoow.protalent.repository;

import com.jaoow.protalent.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    boolean existsByEmail(String email);

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN e.technicalSkills ts " +
            "WHERE (:name IS NULL OR :name = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:skill IS NULL OR :skill = '' OR LOWER(ts.skillName) LIKE LOWER(CONCAT('%', :skill, '%')))")
    List<Employee> searchEmployees(@Param("name") String name, @Param("skill") String skill);

}
