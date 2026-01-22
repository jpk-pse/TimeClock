package com.psedb.timeclock.employee.dto;

public record EmployeeResponseDto (
        Long id,
        String displayName,
        boolean isActive
){
}
