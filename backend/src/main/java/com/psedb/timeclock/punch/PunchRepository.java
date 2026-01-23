package com.psedb.timeclock.punch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PunchRepository extends JpaRepository<Punch, Long> {
    List<Punch> findByEmployeeIdAndPunchedAtBetweenOrderByPunchedAtAsc(
            Long employeeId,
            Instant fromInclusive,
            Instant toExclusive
    );

}
