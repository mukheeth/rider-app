# Troubleshooting: Signup Not Saving to Database

## Issue
After signing up, users are not being saved to the database (0 rows in users table).

## Quick Fix Steps

### Step 1: Restart Backend
The backend MUST be restarted after adding new code:

1. **Stop the current backend:**
   - Press `Ctrl+C` in the terminal where backend is running
   - Or kill the Java process

2. **Restart the backend:**
   ```powershell
   mvn clean compile
   mvn spring-boot:run
   ```

### Step 2: Check Backend Logs
Look for these messages when you register:

**Good signs:**
- `Hibernate: insert into users ...`
- `Hibernate: insert into riders ...`
- No error messages

**Bad signs:**
- `RIDER role not found in database`
- `Transaction rolled back`
- `Could not open connection`
- Any exception stack traces

### Step 3: Verify Database Connection
```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -c "SELECT COUNT(*) FROM roles;"
$env:PGPASSWORD=$null
```

Should return: `3` (RIDER, DRIVER, ADMIN)

### Step 4: Test Registration via API
Test directly to see the error:

```powershell
$body = @{
    email='test@test.com'
    password='Test123456'
    phoneNumber='1234567890'
    firstName='Test'
    lastName='User'
} | ConvertTo-Json

Invoke-WebRequest -Uri 'http://localhost:8080/api/v1/auth/register/rider' `
    -Method POST `
    -Body $body `
    -ContentType 'application/json' `
    -ErrorAction Stop | Select-Object StatusCode, Content
```

## Common Issues

### Issue 1: Backend Not Restarted
**Symptom:** Old code still running
**Fix:** Restart backend completely

### Issue 2: Roles Table Empty
**Symptom:** Error: "RIDER role not found in database"
**Fix:** Run migrations again:
```powershell
powershell -ExecutionPolicy Bypass -File "database\run_migrations.ps1"
```

### Issue 3: Database Connection Failed
**Symptom:** Connection errors in logs
**Fix:** Check database is running and credentials are correct

### Issue 4: Transaction Rollback
**Symptom:** SQL executed but rolled back
**Fix:** Check for constraint violations or foreign key issues

## Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.com.ridehailing.backend=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## Verify Registration Worked

After restarting and registering:

```sql
-- Check users table
SELECT user_id, email, first_name, last_name, status, created_at 
FROM users 
ORDER BY created_at DESC;

-- Check riders table
SELECT rider_id, total_rides, created_at 
FROM riders 
ORDER BY created_at DESC;

-- Check both together
SELECT u.email, u.first_name, u.last_name, r.total_rides, u.created_at
FROM users u
JOIN riders r ON u.user_id = r.rider_id
ORDER BY u.created_at DESC;
```

## Next Steps

1. **Restart backend** (most important!)
2. **Try registering again**
3. **Check backend logs** for SQL statements
4. **Verify in database** using SQL queries above

