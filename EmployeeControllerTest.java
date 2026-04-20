package com.internship.employeeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.employeeapi.dto.EmployeeRequestDTO;
import com.internship.employeeapi.dto.EmployeeResponseDTO;
import com.internship.employeeapi.exception.ResourceNotFoundException;
import com.internship.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@DisplayName("EmployeeController MockMvc Tests")
class EmployeeControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @MockBean  EmployeeService service;

    @Test
    @DisplayName("POST /api/employees — 201 Created")
    void createEmployee_returns201() throws Exception {
        when(service.createEmployee(any())).thenReturn(buildResponse(1L));
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(buildRequest())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("alice@company.com"));
    }

    @Test
    @DisplayName("GET /api/employees/99 — 404 Not Found")
    void getEmployeeById_notFound_returns404() throws Exception {
        when(service.getEmployeeById(99L))
            .thenThrow(new ResourceNotFoundException("Not found: 99"));
        mockMvc.perform(get("/api/employees/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/employees — 400 on blank firstName")
    void createEmployee_blankName_returns400() throws Exception {
        EmployeeRequestDTO bad = buildRequest();
        bad.setFirstName("");
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bad)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/employees/1 — 204 No Content")
    void deleteEmployee_returns204() throws Exception {
        doNothing().when(service).deleteEmployee(1L);
        mockMvc.perform(delete("/api/employees/1"))
            .andExpect(status().isNoContent());
    }

    private EmployeeRequestDTO buildRequest() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Johnson");
        dto.setEmail("alice@company.com");
        dto.setDepartment("Engineering");
        dto.setSalary(new BigDecimal("85000.00"));
        return dto;
    }

    private EmployeeResponseDTO buildResponse(Long id) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(id);
        dto.setFirstName("Alice");
        dto.setLastName("Johnson");
        dto.setEmail("alice@company.com");
        dto.setDepartment("Engineering");
        dto.setSalary(new BigDecimal("85000.00"));
        return dto;
    }
}
