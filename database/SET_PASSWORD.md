# Setting PostgreSQL Password to "root"

## Current Situation

The application is configured to use password "root" for user "postgres", but the connection is failing. You need to set the PostgreSQL password to "root".

## Method 1: Using psql (Recommended)

### Step 1: Connect to PostgreSQL

You'll need to connect to PostgreSQL first. Try one of these methods:

**Option A: If you know the current password:**
```powershell
psql -U postgres -d postgres
```
(Enter your current password when prompted)

**Option B: If PostgreSQL allows local connections without password:**
```powershell
psql -U postgres -d postgres
```
(May work if configured for trust authentication locally)

**Option C: Using Windows Authentication (if configured):**
```powershell
psql -U $env:USERNAME -d postgres
```

### Step 2: Set the Password

Once connected, run:
```sql
ALTER USER postgres PASSWORD 'root';
```

### Step 3: Verify

```sql
\q
```

Then test:
```powershell
$env:PGPASSWORD="root"
psql -U postgres -d postgres -c "SELECT current_user;"
$env:PGPASSWORD=$null
```

## Method 2: Using pgAdmin

1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Right-click on "Login/Group Roles" → "postgres"
4. Go to "Definition" tab
5. Set password to "root"
6. Click "Save"

## Method 3: Using SQL Command Line (if you have admin access)

If you can access PostgreSQL with admin privileges:

```powershell
# Connect as admin (you may need to find the correct way for your setup)
psql -U postgres -d postgres

# Then run:
ALTER USER postgres PASSWORD 'root';
```

## Method 4: Reset via pg_ctl (Advanced)

If you have access to the PostgreSQL installation:

1. Stop PostgreSQL service
2. Edit `pg_hba.conf` to temporarily allow trust authentication
3. Start PostgreSQL
4. Connect and change password
5. Revert `pg_hba.conf` changes
6. Restart PostgreSQL

## Verify Configuration

After setting the password, test the connection:

```powershell
cd database
.\test_db_connection.ps1
```

Or manually:
```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5432 -U postgres -d postgres -c "SELECT version();"
$env:PGPASSWORD=$null
```

## If You Still Can't Connect

1. **Check PostgreSQL service status:**
   ```powershell
   Get-Service | Where-Object {$_.DisplayName -like "*PostgreSQL*"}
   ```

2. **Check pg_hba.conf location:**
   - Usually in: `C:\Program Files\PostgreSQL\18\data\pg_hba.conf`
   - Or: `C:\Users\<YourUser>\AppData\Local\PostgreSQL\data\pg_hba.conf`

3. **Check authentication method:**
   Look for lines like:
   ```
   host    all             all             127.0.0.1/32            md5
   ```
   Should be `md5` or `scram-sha-256` for password authentication.

4. **Try connecting with different credentials:**
   - Maybe the username is different
   - Maybe PostgreSQL was installed with different default settings

## Next Steps After Password is Set

1. **Test connection:**
   ```powershell
   cd database
   .\test_db_connection.ps1
   ```

2. **Create database (if it doesn't exist):**
   ```powershell
   $env:PGPASSWORD="root"
   psql -U postgres -c "CREATE DATABASE ridehailing;"
   $env:PGPASSWORD=$null
   ```

3. **Verify application.properties:**
   Make sure `src/main/resources/application.properties` has:
   ```properties
   spring.datasource.password=root
   ```

4. **Test from Spring Boot:**
   ```powershell
   mvn spring-boot:run
   ```
   Look for successful database connection messages in the logs.

