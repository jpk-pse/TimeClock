package com.psedb.timeclock.employee;

import com.psedb.timeclock.employee.dto.CreateEmployeeRequest;
import com.psedb.timeclock.employee.dto.EmployeeListItemDto;
import com.psedb.timeclock.employee.dto.EmployeeResponseDto;
import com.psedb.timeclock.security.PinService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    public final EmployeeRepository repo;
    public final PinService pinService;

    public EmployeeService(EmployeeRepository repo, PinService pinService) {
        this.repo = repo;
        this.pinService = pinService;
    }

    @Transactional(readOnly = true)
    public List<EmployeeListItemDto> listEmployees() {
        return repo.findAll().stream()
                .map(e -> new EmployeeListItemDto(e.getId(), e.getDisplayName(), e.isActive()))
                .toList();
    }

    @Transactional
    public EmployeeResponseDto createEmployee(CreateEmployeeRequest req){
        byte[] lookup = pinService.lookupKey(req.pin());

        repo.findByPinLookup(lookup).ifPresent(existing -> {
            throw new IllegalStateException("PIN already in use.");
        });

        String pinHash = pinService.bcryptHash(req.pin());

        Employee saved = repo.save(new Employee(req.displayName(), lookup, pinHash));
        return new EmployeeResponseDto(saved.getId(), saved.getDisplayName(), saved.isActive());
    }

}
