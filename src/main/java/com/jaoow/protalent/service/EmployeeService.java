package com.jaoow.protalent.service;

import com.jaoow.protalent.dto.EmployeeDTO;
import com.jaoow.protalent.exception.EmailAlreadyInUseException;
import com.jaoow.protalent.model.Employee;
import com.jaoow.protalent.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new EmailAlreadyInUseException(employeeDTO.getEmail());
        }

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.getTechnicalSkills().forEach(technicalSkill -> technicalSkill.setEmployee(employee));
        employee.getCertifications().forEach(certification -> certification.setEmployee(employee));

        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    public List<EmployeeDTO> searchEmployees(String name, String skill) {
        List<Employee> employees = employeeRepository.searchEmployees(name, skill);

        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
