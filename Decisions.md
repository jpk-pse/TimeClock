# Time Clock App - Decisions

## Architecture
- Single repository (monorepo) with 4 sections:
    - /backend(Spring Boot)
    - /kiosk (React, Vite)
    - /admin (React, Vite)
    - /hardware (Raspberry Pi)

## Backend Stack
- Java 21
- Spring Boot (Web, validation, Security, Data JPA)
- Flyway for database migrations
- MariaDB 8

## Frontend Stack
- React + Vite
- Fetch API (or axios if needed later)
- React router for admin navigation
- Minimal styling:
    - Start with CSS
    - MUI in v2 for components

## Authentication and Security
### Admin
- Spring Security with form login + session cookies

### Kiosk
- PIN is used only to identify employees
- Kiosk calls the backend using a "kiosk device key" header:
    - Header: X-Kiosk-Key: <shared 'secret>
- Backend rate-limits PIN attempts per IP/device
- PIN stored as bcrypt hash

## Time and Source of Truth
- Server time is the official punch time
- Kiosk does not send its own timestamp to prevent clock drift

## Data Model Decisions
- v1: Punches are append only
- v2: Admin edits create an audit log

## Business Rules
- Accept any punch type at any time
- Totals are computed using pairing heuristics in a service layer
- Flag anomalies for admin review

## API Conventions
- REST JSON
- Base path /api
- Versioning /api/v1
- Errors return:
    - timestamp
    - status
    - error
    - message
    - path

## Coding Conventions
- Backend
    - com.pse-db.timeclock
        - config, security, employee, punch, report
-DTOs for API boundaries (this will keep JPA entities from being exposed)
- Controller -> Service -> Repository

## Deployment Notes
- Backend behind a reverse proxy
- MariaDB hosted on local server
- Kiosk runs in fullscreen browser/kiosk mode on RaspberryPi

