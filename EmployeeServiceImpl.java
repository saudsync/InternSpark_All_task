package com.internship.employeeapi.service.impl;

import com.internship.employeeapi.dto.EmployeeRequestDTO;
import com.internship.employeeapi.dto.EmployeeResponseDTO;
import com.internship.employeeapi.entity.Employee;
import com.internship.employeeapi.exception.ResourceNotFoundException;
import com.internship.employeeapi.repository.EmployeeRepository;
import com.internship.employeeapi.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO req) {
        log.debug("createEmployee called with email: {}", req.getEmail());
        if (repository.existsByEmail(req.getEmail())) {
            log.warn("Duplicate email rejected: {}", req.getEmail());
            throw new IllegalArgumentException("Email already in use: " + req.getEmail());
        }
        Employee emp = new Employee(
            req.getFirstName(), req.getLastName(),
            req.getEmail(), req.getDepartment(), req.getSalary()
        );
        Employee saved = repository.save(emp);
        log.info("Employee created — id={}, dept={}", saved.getId(), req.getDepartment());
        return EmployeeResponseDTO.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.debug("getEmployeeById called for id={}", id);
        Employee emp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        log.info("Employee retrieved — id={}", id);
        return EmployeeResponseDTO.from(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        log.debug("getAllEmployees called");
        return repository.findAll().stream()
            .map(EmployeeResponseDTO::from)
            .toList();
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO req) {
        log.debug("updateEmployee called for id={}", id);
        Employee emp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        emp.setFirstName(req.getFirstName());
        emp.setLastName(req.getLastName());
        emp.setEmail(req.getEmail());
        emp.setSalary(req.getSalary());
        Employee saved = repository.save(emp);
        log.info("Employee updated — id={}, dept={}", id, req.getDepartment());
        return EmployeeResponseDTO.from(saved);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.debug("deleteEmployee called for id={}", id);
        if (!repository.existsById(id)) {
            log.error("Delete failed — Employee not found: id={}", id);
            throw new ResourceNotFoundException("Employee not found: " + id);
        }
        repository.deleteById(id);
        log.info("Employee deleted — id={}", id);
    }
}
