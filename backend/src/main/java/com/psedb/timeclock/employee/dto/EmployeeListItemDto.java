package com.psedb.timeclock.employee.dto;

public record EmployeeListItemDto (
    Long id,
    String displayName,
    boolean active
){}

