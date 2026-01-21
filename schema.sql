-- =========================================================
-- Time Clock App - schema.sql (MariaDB 10.5.29 compatible)
-- Stage 0: core tables + indexes (Flyway-ready)
-- =========================================================
-- Notes:
-- - utf8mb4 + utf8mb4_unicode_ci
-- - InnoDB for FK support
-- - Store timestamps as DATETIME(3) (app should write/read UTC)
-- - PIN handling:
--     pin_hash   = bcrypt(pin)
--     pin_lookup = 32-byte lookup key derived in backend using a PEPPER
--                 (recommended: HMAC-SHA256(pin, pepper))
-- =========================================================

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

  -- Fast lookup key for PIN. Store raw 32 bytes.
  -- Backend computes: pin_lookup = HMAC-SHA256(pin, PEPPER)
  pin_lookup BINARY(32) NOT NULL,

  -- Secure PIN verification (bcrypt)
  pin_hash VARCHAR(255) NOT NULL,

  is_active TINYINT(1) NOT NULL DEFAULT 1,

  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),

  -- PIN must be unique per employee (enforced via lookup key)
  UNIQUE KEY uq_employee_pin_lookup (pin_lookup),

  -- Names are NOT unique in real life. Index it instead.
  KEY idx_employee_display_name (display_name),
  KEY idx_employee_active (is_active)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------
-- admin_user
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(80) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
  is_active TINYINT(1) NOT NULL DEFAULT 1,

  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),
  UNIQUE KEY uq_admin_user_username (username),
  KEY idx_admin_active (is_active)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------
-- punch
-- ---------------------------------------------------------
-- punch_type values are enforced in the app; DB stores varchar for clarity.
-- punched_at is server time (source of truth).
CREATE TABLE IF NOT EXISTS punch (
  id BIGINT NOT NULL AUTO_INCREMENT,
  employee_id BIGINT NOT NULL,

  punch_type VARCHAR(20) NOT NULL,
  source VARCHAR(20) NOT NULL,          -- KIOSK or ADMIN

  punched_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

  note VARCHAR(255) NULL,

  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),

  KEY idx_punch_employee_time (employee_id, punched_at),
  KEY idx_punch_time (punched_at),

  CONSTRAINT fk_punch_employee
    FOREIGN KEY (employee_id) REFERENCES employee(id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT

  -- Optional stricter constraints (safe on MariaDB 10.5)
  -- ,CONSTRAINT chk_punch_source CHECK (source IN ('KIOSK','ADMIN'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------
-- audit_log
-- ---------------------------------------------------------
-- details_json stores JSON text (LONGTEXT) for MariaDB compatibility.
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  admin_user_id BIGINT NULL,

  action VARCHAR(50) NOT NULL,          -- e.g. CREATE_EMPLOYEE, EDIT_PUNCH
  entity_type VARCHAR(50) NOT NULL,     -- e.g. EMPLOYEE, PUNCH
  entity_id BIGINT NOT NULL,

  details_json LONGTEXT NULL,

  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

  PRIMARY KEY (id),

  KEY idx_audit_entity (entity_type, entity_id),
  KEY idx_audit_admin (admin_user_id),
  KEY idx_audit_created_at (created_at),

  CONSTRAINT fk_audit_admin
    FOREIGN KEY (admin_user_id) REFERENCES admin_user(id)
    ON DELETE SET NULL
    ON UPDATE RESTRICT

  -- Optional: enforce valid JSON if you want
  -- ,CONSTRAINT chk_audit_details_json CHECK (details_json IS NULL OR JSON_VALID(details_json))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- End
-- =========================================================
