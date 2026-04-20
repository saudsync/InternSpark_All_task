package com.internship.employeeapi.service;

import com.internship.employeeapi.dto.EmployeeRequestDTO;
import com.internship.employeeapi.dto.EmployeeResponseDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO       createEmployee(EmployeeRequestDTO request);
    EmployeeResponseDTO       getEmployeeById(Long id);
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO       updateEmployee(Long id, EmployeeRequestDTO request);
    void                      deleteEmployee(Long id);
}
