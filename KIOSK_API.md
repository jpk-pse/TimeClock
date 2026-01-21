# Kiosk API

All kiosk endpoints require the `X-Kiosk-Key` header.

***

## Create Punch

Records a time punch for an employee identified by PIN.

### Endpoint
```

POST /kiosk/punch

```

### Headers
```

X-Kiosk-Key: <shared-secret>

```

### Request Body
```json
{
  "pin": "1234",
  "punchType": "IN"
}
```


### Response — 201 Created

```json
{
  "punchId": 987,
  "employeeId": 12,
  "displayName": "John Smith",
  "punchType": "IN",
  "punchedAt": "2026-01-21T10:17:05Z"
}
```


### Error Conditions

| Status | Meaning |
| :--: | :-- |
| 400 | Invalid punch type |
| 401 | Missing or invalid kiosk key |
| 404 | PIN not found or employee inactive |


***

## View Current Week (Employee)

Returns punches and totals for the current week for the given PIN.

### Endpoint

```
GET /kiosk/week?pin=1234
```


### Headers

```
X-Kiosk-Key: <shared-secret>
```


### Response — 200 OK

```json
{
  "employeeId": 12,
  "displayName": "John Smith",
  "weekStart": "2026-01-19",
  "weekEnd": "2026-01-25",
  "days": [
    {
      "date": "2026-01-21",
      "punches": [
        { "punchType": "IN", "punchedAt": "2026-01-21T08:01:10Z" },
        { "punchType": "LUNCH_OUT", "punchedAt": "2026-01-21T12:01:00Z" },
        { "punchType": "LUNCH_IN", "punchedAt": "2026-01-21T12:30:00Z" },
        { "punchType": "OUT", "punchedAt": "2026-01-21T17:02:44Z" }
      ],
      "workedMinutes": 511,
      "lunchMinutes": 30,
      "breakMinutes": 0
    }
  ],
  "weekWorkedMinutes": 511
}
```
## Notes
- Server time is used for all punches
- Totals may be approximate
- Invalid punches are not blocked, but they are flagged
