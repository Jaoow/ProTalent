package com.jaoow.protalent.controller;

import com.jaoow.protalent.dto.EmployeeDTO;
import com.jaoow.protalent.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create a new employee", description = "Creates a new employee in the system")
    @ApiResponse(responseCode = "200", description = "Employee successfully created")
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        return ResponseEntity.ok(savedEmployee);
    }

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    @ApiResponse(responseCode = "200", description = "List of all employees")
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search employees by name and skill", description = "Search employees by optional name and skill parameters")
    @ApiResponse(responseCode = "200", description = "List of employees matching the search criteria")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(
            @Parameter(description = "Name of the employee to search for")
            @RequestParam(required = false) String name,
            @Parameter(description = "Skill to search for in employees' technical skills")
            @RequestParam(required = false) String skill) {
        log.info("Searching employees by name: {} and skill: {}", name, skill);
        List<EmployeeDTO> employees = employeeService.searchEmployees(name, skill);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
