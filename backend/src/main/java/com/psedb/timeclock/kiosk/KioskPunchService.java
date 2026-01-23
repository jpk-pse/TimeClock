package com.psedb.timeclock.kiosk;

import com.psedb.timeclock.employee.Employee;
import com.psedb.timeclock.employee.EmployeeRepository;
import com.psedb.timeclock.kiosk.dto.KioskPunchRequest;
import com.psedb.timeclock.kiosk.dto.KioskPunchResponse;
import com.psedb.timeclock.punch.Punch;
import com.psedb.timeclock.punch.PunchRepository;
import com.psedb.timeclock.punch.PunchSource;
import com.psedb.timeclock.punch.PunchType;
import com.psedb.timeclock.security.PinService;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KioskPunchService {

    private final EmployeeRepository employeeRepo;
    private final PinService pinService;
    private final PunchRepository punchRepo;

    public KioskPunchService(EmployeeRepository employeeRepo, PinService pinService, PunchRepository punchRepo) {
        this.employeeRepo = employeeRepo;
        this.pinService = pinService;
        this.punchRepo = punchRepo;
    }

    @Transactional
    public KioskPunchResponse createPunch(KioskPunchRequest req) {
        PunchType type;
        try {
            type = PunchType.valueOf(req.punchType());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid punch type");
        }

        byte[] lookup = pinService.lookupKey(req.pin());

        Employee emp = employeeRepo.findByPinLookup(lookup)
                .filter(Employee::isActive)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("PIN not found or employee inactive"));


        Punch saved = punchRepo.save(new Punch(
                emp,
                type,
                PunchSource.KIOSK,
                Instant.now(),
                null
        ));


        return new KioskPunchResponse(
                saved.getId(),
                emp.getId(),
                emp.getDisplayName(),
                saved.getPunchType().name(),
                saved.getPunchedAt()
        );
    }
}

