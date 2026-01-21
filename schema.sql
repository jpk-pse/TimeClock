-- =========================================================
-- Time Clock App - schema.sql (MariaDB 10.5.29 compatible)
-- =========================================================
-- Notes:
-- - Uses utf8mb4 + utf8mb4_unicode_ci (supported by MariaDB 10.5)
-- - Uses LONGTEXT for JSON payloads (MariaDB JSON is effectively text)
-- - All tables are InnoDB for FK support
-- =========================================================

-- Create database (safe to run multiple times)
CREATE DATABASE IF NOT EXISTS timeclock
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE timeclock;

-- ---------------------------------------------------------
-- employee
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS employee (
  id BIGINT NOT NULL AUTO_INCREMENT,
  display_name VARCHAR(100) NOT NULL,
  pin_hash VARCHAR(255) NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE KEY uq_employee_display_name (display_name)
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- admin_user
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(80) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE KEY uq_admin_user_username (username)
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- punch
-- ---------------------------------------------------------
-- punch_type values enforced in app (enum). Stored as VARCHAR for clarity.
-- punched_at is server time (source of truth).
CREATE TABLE IF NOT EXISTS punch (
  id BIGINT NOT NULL AUTO_INCREMENT,
  employee_id BIGINT NOT NULL,
  punch_type VARCHAR(20) NOT NULL,
  punched_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  source VARCHAR(20) NOT NULL,          -- KIOSK or ADMIN
  note VARCHAR(255) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_punch_employee_time (employee_id, punched_at),
  KEY idx_punch_time (punched_at),

  CONSTRAINT fk_punch_employee
    FOREIGN KEY (employee_id) REFERENCES employee(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------
-- audit_log 
-- ---------------------------------------------------------
-- details_json stores JSON text (LONGTEXT) for maximum MariaDB compatibility.
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  admin_user_id BIGINT NULL,
  action VARCHAR(50) NOT NULL,          -- e.g. CREATE_EMPLOYEE, EDIT_PUNCH
  entity_type VARCHAR(50) NOT NULL,     -- e.g. EMPLOYEE, PUNCH
  entity_id BIGINT NOT NULL,
  details_json LONGTEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_audit_entity (entity_type, entity_id),
  KEY idx_audit_admin (admin_user_id),
  KEY idx_audit_created_at (created_at),

  CONSTRAINT fk_audit_admin
    FOREIGN KEY (admin_user_id) REFERENCES admin_user(id)
    ON DELETE SET NULL
    ON UPDATE RESTRICT

  -- If you want strict JSON validation, uncomment this in MariaDB 10.5:
  -- ,CONSTRAINT chk_audit_details_json CHECK (details_json IS NULL OR JSON_VALID(details_json))
) ENGINE=InnoDB;

-- =========================================================
-- End of schema.sql    
