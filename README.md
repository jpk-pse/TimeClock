# Time Clock App

A simple, self-hosted time clock system consisting of:
- A **kiosk-style touchscreen frontend** for employees to clock in/out using a PIN
- A **desktop admin web portal** for managing employees, reviewing punches, and generating reports
- A **Spring Boot backend API** with a **MariaDB** database

This project is designed to be easy to understand, easy to deploy, and suitable for factory or shop-floor use without subscriptions or cloud dependencies.

---

## Project Goals

- No SaaS subscriptions
- Simple PIN-based employee clock-in/out
- Touchscreen-friendly kiosk UI (800×480)
- Traditional, maintainable backend (Spring Boot + MariaDB)
- Clear separation between kiosk and admin functionality
- Designed for incremental improvement (v1 → v2 → v3)

---

## Architecture Overview

```
┌────────────┐ ┌──────────────┐ ┌────────────┐
│ Kiosk UI │ ---> │ Spring Boot │ ---> │ MariaDB │
│ (React) │ │ API │ │ 10.5.x │
└────────────┘ └──────────────┘ └────────────┘
▲
│
┌────────────┐
│ Admin UI │
│ (React) │
└────────────┘
```

---

## Repository Structure

```
/
├── backend/ # Spring Boot backend API
├── kiosk/ # React kiosk frontend (800x480 touchscreen)
├── admin/ # React admin frontend (desktop)
├── schema.sql # MariaDB schema (v1)
├── Requirements.md # Functional and non-functional requirements
├── Decisions.md # Architecture and design decisions
├── TimeClock_API_v1.md # API contract (source of truth)
└── README.md # This file
```

---

## Technology Stack

### Backend
- Java 21
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring Security
- MariaDB 10.5.x
- Flyway (planned)

### Frontend
- React
- Vite
- Plain CSS (initially, for predictability)

### Database
- MariaDB 10.5.29
- utf8mb4 / InnoDB

---

## Core Features (v1)

### Kiosk
- PIN entry via touchscreen
- Clock actions:
  - IN / OUT
  - LUNCH IN / LUNCH OUT
  - BREAK IN / BREAK OUT
- View current week punches and totals
- Server-side timestamps (no kiosk clock drift)
- Auto-reset to PIN screen after punch

### Admin
- Admin login
- Employee management (create, activate/deactivate, reset PIN)
- View punches by employee and date range
- Weekly totals and basic anomaly flags
- CSV export (planned)

---

## Security Model

- **Admin**
  - Username/password
  - Session-based authentication (Spring Security)

- **Kiosk**
  - Shared kiosk device key (`X-Kiosk-Key`)
  - PINs are **hashed** (never stored or transmitted in plaintext beyond initial request)
  - Server time is the source of truth

---

## API Contract

The backend API is defined in:

TimeClock_API_v1.md


This file is the **authoritative contract** between frontend and backend.

If behavior changes:
1. Update the API contract
2. Then update code

---

## Database Schema

The MariaDB schema is defined in:

schema.sql


- Compatible with MariaDB 10.5.x
- Uses InnoDB and foreign keys
- Designed to allow future audit logging and reporting without schema rewrites

---

## Development Philosophy

- Start simple, don’t over-engineer
- Accept punches first, flag problems later
- Favor clarity over cleverness
- Traditional, boring tech that works

This is intentional.

---

## Status

🚧 **Work in progress**

Current focus:
- Backend foundation
- Kiosk punch flow
- Admin employee management

---

## License

Internal / Private project (update if open-sourcing later)
