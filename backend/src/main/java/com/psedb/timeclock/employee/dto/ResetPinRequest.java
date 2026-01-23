package com.psedb.timeclock.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPinRequest(
        @NotBlank @Pattern(regexp = "\\d{4,8}") String pin
) {
}
