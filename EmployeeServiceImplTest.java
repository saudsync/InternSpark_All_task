package com.internship.employeeapi.service;

import com.internship.employeeapi.dto.EmployeeRequestDTO;
import com.internship.employeeapi.dto.EmployeeResponseDTO;
import com.internship.employeeapi.entity.Employee;
import com.internship.employeeapi.exception.ResourceNotFoundException;
import com.internship.employeeapi.repository.EmployeeRepository;
import com.internship.employeeapi.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Unit Tests")
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee sampleEmployee;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setId(1L);
        sampleEmployee.setFirstName("Alice");
        sampleEmployee.setLastName("Johnson");
        sampleEmployee.setEmail("alice@company.com");
        sampleEmployee.setSalary(new BigDecimal("85000.00"));

        requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Alice");
        requestDTO.setLastName("Johnson");
        requestDTO.setEmail("alice@company.com");
        requestDTO.setDepartment("Engineering");
        requestDTO.setSalary(new BigDecimal("85000.00"));
    }

    @Test
    @DisplayName("createEmployee: success — returns DTO with generated ID")
    void createEmployee_success() {
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeResponseDTO result = service.createEmployee(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("alice@company.com");
        verify(repository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("createEmployee: duplicate email — throws IllegalArgumentException")
    void createEmployee_duplicateEmail_throwsException() {
        when(repository.existsByEmail("alice@company.com")).thenReturn(true);

        assertThatThrownBy(() -> service.createEmployee(requestDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Email already in use");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("getEmployeeById: found — returns correct DTO")
    void getEmployeeById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        EmployeeResponseDTO result = service.getEmployeeById(1L);
        assertThat(result.getFirstName()).isEqualTo("Alice");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("getEmployeeById: not found — throws ResourceNotFoundException")
    void getEmployeeById_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getEmployeeById(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("getAllEmployees: returns list of mapped DTOs")
    void getAllEmployees_returnsList() {
        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setFirstName("Bob");
        when(repository.findAll()).thenReturn(List.of(sampleEmployee, emp2));
        List<EmployeeResponseDTO> result = service.getAllEmployees();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("getAllEmployees: empty DB — returns empty list")
    void getAllEmployees_empty() {
        when(repository.findAll()).thenReturn(List.of());
        assertThat(service.getAllEmployees()).isEmpty();
    }

    @Test
    @DisplayName("updateEmployee: success — fields updated and saved")
    void updateEmployee_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        requestDTO.setDepartment("Architecture");
        requestDTO.setSalary(new BigDecimal("95000.00"));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeResponseDTO result = service.updateEmployee(1L, requestDTO);
        assertThat(result.getSalary()).isEqualByComparingTo("95000.00");
        verify(repository).save(any(Employee.class));
    }

    @Test
    @DisplayName("deleteEmployee: success — deleteById called once")
    void deleteEmployee_success() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        service.deleteEmployee(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEmployee: not found — throws, deleteById never called")
    void deleteEmployee_notFound() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteEmployee(99L))
            .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }
}
