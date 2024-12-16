package com.jaoow.protalent.service;

import com.jaoow.protalent.dto.EmployeeDTO;
import com.jaoow.protalent.dto.EmployeeFilterDTO;
import com.jaoow.protalent.exception.EmailAlreadyInUseException;
import com.jaoow.protalent.exception.EmployeeNotFoundException;
import com.jaoow.protalent.model.Certification;
import com.jaoow.protalent.model.Employee;
import com.jaoow.protalent.model.Language;
import com.jaoow.protalent.model.TechnicalSkill;
import com.jaoow.protalent.repository.EmployeeRepository;
import com.jaoow.protalent.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Cacheable(value = "employees", key = "'allEmployees'")
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees from database.");
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "employees", key = "'filteredEmployees' + #filterDTO.hashCode()")
    public List<EmployeeDTO> getFilteredEmployees(EmployeeFilterDTO filterDTO) {
        log.info("Fetching filtered employees from database.");
        return employeeRepository.findAll(EmployeeSpecification.filter(filterDTO)).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "employees", key = "'employee' + #id")
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        log.info("Fetching employee with ID {} from database.", id);
        return employeeRepository.findById(id).map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
        log.info("Employee with ID {} deleted and cache cleared.", id);
    }

    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmailIgnoreCase(employeeDTO.getEmail())) {
            throw new EmailAlreadyInUseException(employeeDTO.getEmail());
        }

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.getTechnicalSkills().forEach(technicalSkill -> technicalSkill.setEmployee(employee));
        employee.getCertifications().forEach(certification -> certification.setEmployee(employee));
        employee.getLanguages().forEach(language -> language.setEmployee(employee));

        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (!employee.getEmail().equals(employeeDTO.getEmail())
                && employeeRepository.existsByEmailIgnoreCase(employeeDTO.getEmail())) {
            throw new EmailAlreadyInUseException(employeeDTO.getEmail());
        }

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setContact(employeeDTO.getContact());
        employee.setExperienceYears(employeeDTO.getExperienceYears());
        employee.setLinkedinUrl(employeeDTO.getLinkedinUrl());

        List<TechnicalSkill> updatedTechnicalSkills = employeeDTO.getTechnicalSkills().stream()
                .map(technicalSkillDTO -> modelMapper.map(technicalSkillDTO, TechnicalSkill.class))
                .peek(technicalSkill -> technicalSkill.setEmployee(employee))
                .toList();

        employee.getTechnicalSkills().clear();
        employee.getTechnicalSkills().addAll(updatedTechnicalSkills);

        List<Certification> updatedCertifications = employeeDTO.getCertifications().stream()
                .map(certificationDTO -> modelMapper.map(certificationDTO, Certification.class))
                .peek(certification -> certification.setEmployee(employee))
                .toList();

        employee.getCertifications().clear();
        employee.getCertifications().addAll(updatedCertifications);

        List<Language> updatedLanguages = employeeDTO.getLanguages().stream()
                .map(languageDTO -> modelMapper.map(languageDTO, Language.class))
                .peek(language -> language.setEmployee(employee))
                .toList();

        employee.getLanguages().clear();
        employee.getLanguages().addAll(updatedLanguages);

        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }
}
