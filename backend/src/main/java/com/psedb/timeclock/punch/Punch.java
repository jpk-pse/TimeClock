package com.psedb.timeclock.punch;

import com.psedb.timeclock.employee.Employee;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "punch")
public class Punch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // punch.employee_id FK -> employee.id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "punch_type", nullable = false, length = 20)
    private PunchType punchType;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 20)
    private PunchSource source;

    @Column(name = "punched_at", nullable = false)
    private Instant punchedAt;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Punch() { }

    public Punch(Employee employee, PunchType punchType, PunchSource source, Instant punchedAt, String note) {
        this.employee = employee;
        this.punchType = punchType;
        this.source = source;
        this.punchedAt = punchedAt;
        this.note = note;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (punchedAt == null) punchedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public PunchType getPunchType() { return punchType; }
    public PunchSource getSource() { return source; }
    public Instant getPunchedAt() { return punchedAt; }
    public String getNote() { return note; }
    public Instant getCreatedAt() { return createdAt; }
}
