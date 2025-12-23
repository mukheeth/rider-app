# Database Connection Test Commands

## Quick Connection Test

### Test 1: Check if PostgreSQL is running and accessible

```bash
psql -U postgres -c "SELECT version();"
```

If this works, PostgreSQL is running and accessible.

### Test 2: Check if the database exists

```bash
psql -U postgres -c "\l" | findstr ridehailing
```

Or on Linux/Mac:
```bash
psql -U postgres -c "\l" | grep ridehailing
```

### Test 3: Try to connect to the ridehailing database

```bash
psql -U postgres -d ridehailing -c "SELECT current_database(), version();"
```

**Expected Results:**
- ✅ If database exists: Shows database name and PostgreSQL version
- ❌ If database doesn't exist: Error "database 'ridehailing' does not exist"

### Test 4: Full connection test with all credentials

```bash
psql -h localhost -p 5432 -U postgres -d ridehailing -c "SELECT 1 as connection_test;"
```

**Note:** You'll be prompted for the password. The password should be `postgres` as configured in `application.properties`.

## Create Database (if it doesn't exist)

If Test 3 fails with "database does not exist", create it:

```bash
psql -U postgres -c "CREATE DATABASE ridehailing;"
```

Then verify:
```bash
psql -U postgres -d ridehailing -c "SELECT current_database();"
```

## Test from Spring Boot Application

You can also test the connection by starting the Spring Boot app:

```bash
mvn spring-boot:run
```

Check the console output for:
- ✅ "HikariPool-1 - Starting..." (connection pool started successfully)
- ❌ "Connection refused" or "database does not exist" (connection failed)

## PowerShell Script (Windows)

Save this as `test-db.ps1`:

```powershell
Write-Host "Testing PostgreSQL connection..." -ForegroundColor Cyan

# Test 1: Check if psql is available
try {
    $version = psql --version
    Write-Host "✓ PostgreSQL client found: $version" -ForegroundColor Green
} catch {
    Write-Host "✗ PostgreSQL client not found" -ForegroundColor Red
    exit 1
}

# Test 2: Try to connect
$env:PGPASSWORD = "postgres"
try {
    $result = psql -h localhost -p 5432 -U postgres -d ridehailing -c "SELECT current_database(), version();" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Successfully connected to 'ridehailing' database" -ForegroundColor Green
        Write-Host $result
    } else {
        Write-Host "✗ Connection failed or database doesn't exist" -ForegroundColor Red
        Write-Host $result
        Write-Host "`nTry creating the database with:" -ForegroundColor Yellow
        Write-Host "  psql -U postgres -c 'CREATE DATABASE ridehailing;'" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
} finally {
    $env:PGPASSWORD = $null
}
```

Run it:
```powershell
.\test-db.ps1
```


