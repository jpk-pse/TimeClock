package com.psedb.timeclock.employee;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


/**
 * Represents an Employee entity stored in the database.
 *
 * This class is annotated as an entity with table-level constraints and indexing
 * to optimize database operations and maintain data integrity. It includes details
 * such as display name, credential data for secure PIN management, and the
 * employee's active status.
 *
 * Fields:
 * - id: Unique identifier for the employee.
 * - displayName: The display name of the employee, stored as a non-nullable string of maximum length 100.
 * - pinLookup: A binary field used for referencing the employee securely.
 * - pinHash: A hash representing the employee's secure credential.
 * - isActive: A flag indicating if the employee is active.
 * - createdAt: Timestamp of when the record was created, managed by the database.
 * - updatedAt: Timestamp of the last update to the record, managed by the database.
 *
 * Constructors:
 * The no-args constructor is protected and intended for use by frameworks like JPA.
 * A parameterized constructor allows for the creation of an employee with a display name,
 * pin lookup, and pin hash while defaulting to an active status.
 *
 * Annotations:
 * - @Entity: Marks this class as a JPA entity.
 * - @Table: Specifies table-level constraints like indexes and unique constraints.
 * - @Getter: Indicates the presence of getter methods for fields.
 * - @Setter: Allows modification of specific fields.
 * - @NoArgsConstructor: Provides a no-arguments constructor with protected visibility.
 *
 * Database Constraints:
 * - Unique constraint on the `pin_lookup` column to ensure secure and unique employee identification.
 * - Indexes on the `display_name` column for faster retrieval and on the `is_active` column for
 *   efficient filtering of active employees.
 */
@Entity
@Table(name = "employee",
        indexes = {
                @Index(name = "idx_employee_display_name", columnList = "display_name"),
                @Index(name = "idx_employee_active", columnList = "is_active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_employee_pin_lookup", columnNames = "pin_lookup")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Setter
        @Column(name = "display_name", nullable = false, length = 100)
        private String displayName;

        @Setter
        @Column(name = "pin_lookup", nullable = false, columnDefinition = "BINARY(32)")
        private byte[] pinLookup;

        @Setter
        @Column(name = "pin_hash", nullable = false, length = 255)
        private String pinHash;

        @Setter
        @Column(name = "is_active", nullable = false)
        private boolean isActive = true;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

        public Employee(String displayName, byte[] pinLookup, String pinHash) {
                this.displayName = displayName;
                this.pinLookup = pinLookup;
                this.pinHash = pinHash;
                this.isActive = true;
        }

}
