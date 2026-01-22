package com.psedb.timeclock.employee;

import com.psedb.timeclock.employee.dto.CreateEmployeeRequest;
import com.psedb.timeclock.employee.dto.EmployeeListItemDto;
import com.psedb.timeclock.employee.dto.EmployeeResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/employees")
public class EmployeeAdminController {

    private final EmployeeService employeeService;

    public EmployeeAdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeListItemDto> listEmployees() {
        return employeeService.listEmployees();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto createEmployee(@Valid @RequestBody CreateEmployeeRequest req) {
        return employeeService.createEmployee(req);
    }
}
