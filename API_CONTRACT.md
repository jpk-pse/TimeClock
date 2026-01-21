# Time Clock API Contract (v1)

Base URL: `/api/v1`
All requests/response are JSON unless noted.

## Authentication

### Admin
- Form login session (traditional)
- Admin endpoints require authenticated admin session cookie.

### Kiosk
- Kiosk requests must include header:
  - `X-Kiosk-Key: <shared-secret>`
- Kiosk endpoints do not require admin login.

---

## Common Models

### PunchType
`IN | OUT | LUNCH_IN | LUNCH_OUT | BREAK_IN | BREAK_OUT`

### Error Response (typical)
```json
{
  "timestamp": "2026-01-21T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "PIN invalid",
  "path": "/api/v1/kiosk/auth"
}
