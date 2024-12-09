package com.jaoow.protalent.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Object id) {
        super("Employee not found with ID " + id);
    }
}
