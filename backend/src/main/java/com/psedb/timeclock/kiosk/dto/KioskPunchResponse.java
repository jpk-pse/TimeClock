package com.psedb.timeclock.kiosk.dto;

import java.time.Instant;

public record KioskPunchResponse(
        Long punchId,
        Long employeeId,
        String displayName,
        String punchType,
        Instant punchedAt
) {
}
