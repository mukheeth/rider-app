# Database Connection Guide

## Current Status

**⚠️ Database Connection Issue Detected**

Based on the connection test, PostgreSQL server is running but authentication failed. This guide will help you diagnose and fix the connection issue.

## Quick Diagnosis

### Step 1: Run the Connection Test Script

Run the PowerShell test script:

```powershell
cd database
.\test_db_connection.ps1
```

This script will:
- ✅ Check if PostgreSQL client is installed
- ✅ Check if PostgreSQL server is running
- ✅ Test connection with current credentials
- ✅ Check if database exists
- ✅ List tables (if any)

### Step 2: Check Current Configuration

Your application is configured to connect to:
- **Host:** `localhost`
- **Port:** `5432`
- **Database:** `ridehailing`
- **Username:** `postgres`
- **Password:** `postgres`

Configuration file: `src/main/resources/application.properties`

## Common Issues and Solutions

### Issue 1: Password Authentication Failed

**Symptoms:**
```
FATAL: password authentication failed for user "postgres"
```

**Solutions:**

#### Option A: Reset PostgreSQL Password

1. Connect to PostgreSQL (you may need to use Windows authentication or find the correct password):
   ```powershell
   psql -U postgres
   ```
   
   If that doesn't work, try:
   ```powershell
   psql -U postgres -d postgres
   ```

2. Once connected, reset the password:
   ```sql
   ALTER USER postgres PASSWORD 'postgres';
   ```

3. Exit:
   ```sql
   \q
   ```

#### Option B: Update Application Configuration

If you know the correct password, update `src/main/resources/application.properties`:

```properties
spring.datasource.password=your_actual_password
```

#### Option C: Check pg_hba.conf

The PostgreSQL authentication configuration file (`pg_hba.conf`) might be restricting access. Find it:

```powershell
# Usually located in PostgreSQL data directory
# Common locations:
# C:\Program Files\PostgreSQL\18\data\pg_hba.conf
# C:\Users\<YourUser>\AppData\Local\PostgreSQL\data\pg_hba.conf
```

Look for a line like:
```
host    all             all             127.0.0.1/32            md5
```

Make sure it allows password authentication (`md5` or `scram-sha-256`).

### Issue 2: Database Doesn't Exist

**Symptoms:**
```
FATAL: database "ridehailing" does not exist
```

**Solution:**

Create the database:

```powershell
psql -U postgres -c "CREATE DATABASE ridehailing;"
```

Or connect to PostgreSQL and create it:

```powershell
psql -U postgres
```

Then:
```sql
CREATE DATABASE ridehailing;
\q
```

### Issue 3: PostgreSQL Service Not Running

**Symptoms:**
```
connection to server at "localhost" (::1), port 5432 failed
```

**Solutions:**

#### Check if PostgreSQL service is running:

```powershell
Get-Service -Name "*postgres*"
```

#### Start PostgreSQL service:

```powershell
# Find the service name first
Get-Service | Where-Object {$_.DisplayName -like "*PostgreSQL*"}

# Start it (replace with actual service name)
Start-Service postgresql-x64-18
```

Or use Services GUI:
1. Press `Win + R`, type `services.msc`
2. Find PostgreSQL service (e.g., "postgresql-x64-18")
3. Right-click → Start

### Issue 4: Wrong Port

**Symptoms:**
```
connection refused
```

**Check:**

```powershell
# Check what port PostgreSQL is actually using
Get-NetTCPConnection -LocalPort 5432 -ErrorAction SilentlyContinue
```

If PostgreSQL is on a different port, update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:YOUR_PORT/ridehailing
```

## Testing Connection

### Method 1: PowerShell Script (Recommended)

```powershell
cd database
.\test_db_connection.ps1
```

### Method 2: Manual psql Test

```powershell
# Set password as environment variable
$env:PGPASSWORD="postgres"

# Test connection
psql -h localhost -p 5432 -U postgres -d ridehailing -c "SELECT current_database(), version();"

# Clean up
$env:PGPASSWORD=$null
```

### Method 3: Java Test Class

Run the database connection test:

```powershell
mvn test -Dtest=DatabaseConnectionTest
```

This will test:
- ✅ DataSource configuration
- ✅ Database connection
- ✅ Database type (PostgreSQL)
- ✅ Database name
- ✅ Table existence

### Method 4: Start Spring Boot Application

Start the application and check logs:

```powershell
mvn spring-boot:run
```

**Success indicators:**
- ✅ `HikariPool-1 - Starting...`
- ✅ `HikariPool-1 - Start completed.`
- ✅ No connection errors

**Failure indicators:**
- ❌ `Connection refused`
- ❌ `password authentication failed`
- ❌ `database "ridehailing" does not exist`

## Step-by-Step Setup (If Starting Fresh)

### 1. Install PostgreSQL (if not installed)

Download from: https://www.postgresql.org/download/windows/

During installation:
- Remember the password you set for `postgres` user
- Note the port (default: 5432)
- Note the data directory location

### 2. Verify Installation

```powershell
psql --version
```

### 3. Start PostgreSQL Service

```powershell
Get-Service | Where-Object {$_.DisplayName -like "*PostgreSQL*"} | Start-Service
```

### 4. Set Password (if needed)

```powershell
psql -U postgres
```

```sql
ALTER USER postgres PASSWORD 'postgres';
\q
```

### 5. Create Database

```powershell
psql -U postgres -c "CREATE DATABASE ridehailing;"
```

### 6. Verify Database

```powershell
psql -U postgres -c "\l" | Select-String "ridehailing"
```

### 7. Test Connection

```powershell
cd database
.\test_db_connection.ps1
```

### 8. Update Application Properties (if needed)

If your PostgreSQL setup uses different credentials, update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ridehailing
spring.datasource.username=postgres
spring.datasource.password=your_password_here
```

### 9. Test from Application

```powershell
mvn spring-boot:run
```

Check for successful connection in the logs.

## Next Steps After Successful Connection

Once the database connection is working:

1. **Create Tables:**
   - Run migration scripts from `database/` directory
   - Or set `spring.jpa.hibernate.ddl-auto=create` temporarily (development only)

2. **Verify Tables:**
   ```powershell
   psql -U postgres -d ridehailing -c "\dt"
   ```

3. **Start Application:**
   ```powershell
   mvn spring-boot:run
   ```

## Troubleshooting Checklist

- [ ] PostgreSQL is installed
- [ ] PostgreSQL service is running
- [ ] Port 5432 is accessible
- [ ] Username and password are correct
- [ ] Database `ridehailing` exists
- [ ] `pg_hba.conf` allows password authentication
- [ ] Application properties match PostgreSQL configuration
- [ ] Firewall is not blocking port 5432

## Getting Help

If you're still having issues:

1. Check PostgreSQL logs (usually in data directory):
   ```
   C:\Program Files\PostgreSQL\18\data\log\
   ```

2. Enable SQL logging in application.properties:
   ```properties
   spring.jpa.show-sql=true
   ```

3. Check Spring Boot startup logs for detailed error messages

4. Verify PostgreSQL version compatibility:
   - Application uses PostgreSQL driver 42.7.1
   - Compatible with PostgreSQL 9.4+

## Additional Resources

- PostgreSQL Documentation: https://www.postgresql.org/docs/
- Spring Boot Database Configuration: https://spring.io/guides/gs/accessing-data-jpa/
- HikariCP (Connection Pool): https://github.com/brettwooldridge/HikariCP

