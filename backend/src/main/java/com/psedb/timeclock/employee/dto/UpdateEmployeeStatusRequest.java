package com.psedb.timeclock.employee.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeStatusRequest (
        @NotNull Boolean isActive
){
}
