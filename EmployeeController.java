package com.internship.employeeapi.controller;

import com.internship.employeeapi.dto.EmployeeRequestDTO;
import com.internship.employeeapi.dto.EmployeeResponseDTO;
import com.internship.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // CREATE — POST /api/employees
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(
            @Valid @RequestBody EmployeeRequestDTO request) {
        EmployeeResponseDTO created = service.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL — GET /api/employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    // READ ONE — GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEmployeeById(id));
    }

    // UPDATE — PUT /api/employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO request) {
        return ResponseEntity.ok(service.updateEmployee(id, request));
    }

    // DELETE — DELETE /api/employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
