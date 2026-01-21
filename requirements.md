# Time Clock App - Requirements

## Purpose
Provide a simple kiosk-style time clock for employees to clock in/ou using a PIN on an 800x600 touchscreen, and an admin web portal for managing employees, punches, totals, and reports. All punches are stored in MySQL via a Spring Boot API.

## Users
### Employee (Kiosk)
- Identifies with a numeric PIN
- Can create punches and view their own current pay period time

### Admin
- Authenticates with a username/password
- Manages employees and reviews/edits punches
- Generates reports (CSV initially)

## Kiosk (800x400) Requirement
### Functional
1. Pin Entry
    - Numeric keypad input
    - Clear/Backspace
    - Submit PIN
2. After successful PIN, show punch type choices:
    - IN
    - OUT
    - LUNCH_IN
    - LUNCH_OUT
    - BREAK_IN
    - BREAK_OUT
    - VIEW_PERIOD (read only)
3. Punch Submission
    - Kiosk creates a punch record with timestamp (server time)
    - Shows a success or error message
    - Auto-return to PIN screen after a successful transaction (~5 seconds)
4. View period
    - Shows the current pay period punches along with the daily/period totals
### Non-Functional
- UI must be touch friendly
- Must operate on a kiosk device that can be locked down, preferably a Raspberry Pi with a touchscreen
- Avoids complex flows. 
- Do not block punches done out of order, only flag them for admin to fix later

## Admin Portal
### Functional
1. Authentication
    - Admin login via username/password - Spring Security
2. Employees
    - Create employee (name, PIN, shift)
    - Activate/deactivate employee
    - Reset PIN
3. Time Review
   - View punches by employee and by date range/week
   - View daily totals and weekly totals
4. Corrections
   - Add/edit/delete punch with reason
   - Audit log of changes
5. Reports
   - Export punches and totals to CSV (PDF later)

### Non-Functional
- Role-based access
- Every edit should be auditable

## Data Requirements
- Store employees with PIN is a secure form (BCrypt)
- Store punches as append-only in v1 (admin edits come later)
- Store who/what created each punch (KIOSK vs ADMIN)

## Future Features (make sure to plan for)
- Complex overtime and pay rules
- Multi-location support
- Biometric/RFID card in addition to PIN
- Payroll integration
- Offline mode for kiosk

## Milestones for v1
- Kiosk app can record punches to DB via backend API using PIN
- Kiosk can show current pay period punches for the PIN
- Admin can log in, manage employees and view punches/totals
- Basic CSV export