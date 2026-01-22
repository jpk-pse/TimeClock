package com.psedb.timeclock.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateEmployeeRequest (
        @NotBlank
        @Size(max=100)
        String displayName,

        @NotBlank
        @Pattern(regexp = "\\d{4,8}")
        String pin
){
}
