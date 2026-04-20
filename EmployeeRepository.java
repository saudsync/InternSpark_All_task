package com.internship.employeeapi.repository;

import com.internship.employeeapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartmentName(String departmentName);

    boolean existsByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(@Param("min") BigDecimal min,
                                     @Param("max") BigDecimal max);

    @Query(value = "SELECT * FROM employees WHERE hire_date >= :date", nativeQuery = true)
    List<Employee> findRecentHires(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :deptId")
    Long countByDepartmentId(@Param("deptId") Long deptId);
}
