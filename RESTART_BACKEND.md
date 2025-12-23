# ⚠️ CRITICAL: Restart Backend Required

## The Error You're Seeing

**Error:** `null Identifier (com.ridehailing.backend.entity.Rider)`

This error occurs because the backend is running **old code** that doesn't have the fixes for the Rider entity.

## ✅ Solution: Restart Backend

### Step 1: Stop Current Backend
1. Find the terminal/PowerShell window where backend is running
2. Press `Ctrl+C` to stop it
3. Wait for it to fully stop

### Step 2: Restart Backend
```powershell
# Navigate to project root (if not already there)
cd C:\New_uber

# Clean and compile
mvn clean compile

# Start backend
mvn spring-boot:run
```

### Step 3: Wait for Startup
Look for these messages in the logs:
- ✅ `Started RideHailingApplication`
- ✅ `HikariPool-1 - Start completed`
- ✅ No errors about Rider entity

### Step 4: Test Registration Again
After backend is running, try registering:
- Email: `user2@test.com`
- Password: `SecurePass789`
- Phone: `2222222222`
- First Name: `User`
- Last Name: `Two`

## Why This Happens

The code has been fixed, but:
- **Backend needs restart** to load new code
- **Old code** is still running in memory
- **New code** is on disk but not loaded yet

## Verification

After restarting, check backend logs for:
```
Hibernate: insert into users ...
Hibernate: insert into riders ...
```

If you see these SQL statements, registration is working!

## Still Getting Error?

If you still get the error after restarting:

1. **Check backend logs** for the full error stack trace
2. **Verify database tables exist:**
   ```powershell
   $env:PGPASSWORD="root"
   psql -h localhost -p 5433 -U postgres -d ridehailing -c "\dt"
   $env:PGPASSWORD=$null
   ```

3. **Check roles exist:**
   ```powershell
   $env:PGPASSWORD="root"
   psql -h localhost -p 5433 -U postgres -d ridehailing -c "SELECT * FROM roles;"
   $env:PGPASSWORD=$null
   ```

4. **Share the full error** from backend logs

