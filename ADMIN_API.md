# Admin API

All admin endpoints require an authenticated admin session.

## Employees
### List Employees
```
GET /admin/employees
```
### Response — 200 OK
```json
[
  {
    "id": 12,
    "displayName": "John Smith",
    "isActive": true
  },
  {
    "id": 13,
    "displayName": "Mary Jones",
    "isActive": false
  }
]
```

### Create Employee

```
POST /admin/employees
```

### Request Body

```json
{
  "displayName": "John Smith",
  "pin": "1234"
}
```

### Response — 201 Created

```json
{
  "id": 12,
  "displayName": "John Smith",
  "isActive": true
}
```

### Update Employee Status

```
PATCH /admin/employees/{id}
```

### Request Body

```json
{
  "isActive": false
}
```

### Response — 200 OK

```json
{
  "id": 12,
  "displayName": "John Smith",
  "isActive": false
}
```

### Reset Employee PIN

```
POST /admin/employees/{id}/reset-pin
```

### Request Body

```json
{
  "pin": "4321"
}
```

### Response — 200 OK

```json
{
  "id": 12,
  "displayName": "John Smith"
}
```

### Punch Review
**Get Punches by Employee and Date Range***

```
GET /admin/punches?employeeId=12&from=2026-01-19&to=2026-01-25
```

### Response — 200 OK

```json
{
  "employeeId": 12,
  "from": "2026-01-19",
  "to": "2026-01-25",
  "punches": [
    {
      "id": 987,
      "punchType": "IN",
      "punchedAt": "2026-01-21T08:01:10Z",
      "source": "KIOSK"
    }
  ]
}
```

### Reports
**Weekly Summary**

```
GET /admin/reports/weekly?employeeId=12&weekStart=2026-01-19
```

### Response — 200 OK

```json
{
  "employeeId": 12,
  "weekStart": "2026-01-19",
  "weekWorkedMinutes": 2240,
  "days": [
    { "date": "2026-01-19", "workedMinutes": 480 },
    { "date": "2026-01-20", "workedMinutes": 480 }
  ],
  "flags": [
    "MISSING_OUT_PUNCH:2026-01-20"
  ]
}
```

### Weekly CSV Export

```
GET /admin/reports/weekly.csv?weekStart=2026-01-19
```

Response:

```
Content-Type: text/csv
```

### HTTP Status Codes

|Code|	Meaning|
|----|---------|
|200|	OK|
|201|	Created|
|400|	Validation error|
|401|	Unauthorized|
|403|	Forbidden|
|404|	Not found|