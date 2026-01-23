package com.psedb.timeclock.employee;

import com.psedb.timeclock.employee.dto.CreateEmployeeRequest;
import com.psedb.timeclock.employee.dto.EmployeeListItemDto;
import com.psedb.timeclock.employee.dto.EmployeeResponseDto;
import com.psedb.timeclock.security.PinService;
import com.psedb.timeclock.employee.dto.UpdateEmployeeStatusRequest;
import com.psedb.timeclock.employee.dto.ResetPinRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

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

    @Transactional
    public EmployeeResponseDto updateStatus(Long id, UpdateEmployeeStatusRequest req){
        Employee e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found."));

        e.setActive(req.isActive());

        return new EmployeeResponseDto(e.getId(), e.getDisplayName(), e.isActive());
    }

    @Transactional
    public EmployeeResponseDto resetPin(Long id, ResetPinRequest req){
        Employee e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found."));

        byte[] lookup = pinService.lookupKey(req.pin());

        repo.findByPinLookup(lookup).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalStateException("PIN already in use.");
            }
        });

        e.setPinLookup(lookup);
        e.setPinHash(pinService.bcryptHash(req.pin()));

        return new EmployeeResponseDto(e.getId(), e.getDisplayName(), e.isActive());
    }

}
