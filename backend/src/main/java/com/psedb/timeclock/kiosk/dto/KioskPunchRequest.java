package com.psedb.timeclock.kiosk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record KioskPunchRequest (
        @NotBlank
        @Pattern(regexp = "^[0-9]{3,10}$", message = "PIN must be numeric")
        String pin,

        @NotBlank
        String punchType
){
}
