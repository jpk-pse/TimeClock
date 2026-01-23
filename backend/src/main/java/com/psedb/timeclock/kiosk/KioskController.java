package com.psedb.timeclock.kiosk;

import com.psedb.timeclock.kiosk.dto.KioskPunchRequest;
import com.psedb.timeclock.kiosk.dto.KioskPunchResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kiosk")
public class KioskController {

    private final KioskPunchService kioskPunchService;

    public KioskController(KioskPunchService kioskPunchService) {
        this.kioskPunchService = kioskPunchService;
    }

    @PostMapping("/punch")
    @ResponseStatus(HttpStatus.CREATED)
    public KioskPunchResponse punch(@Valid @RequestBody KioskPunchRequest req) {
        return kioskPunchService.createPunch(req);
    }
}
