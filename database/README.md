# Database Setup Guide

## Current Status

The database **has NOT been created yet**. The application is configured to connect to:
- **Database:** `ridehailing`
- **Host:** `localhost:5432`
- **Username:** `postgres`
- **Password:** `postgres`

## Check if Database Exists

### Option 1: Using psql (PostgreSQL Command Line)

1. **Open psql:**
   ```bash
   psql -U postgres
   ```

2. **Check if database exists:**
   ```sql
   SELECT datname FROM pg_database WHERE datname = 'ridehailing';
   ```

3. **If no rows returned, the database doesn't exist.**

### Option 2: Using pgAdmin or DBeaver

1. Connect to PostgreSQL server
2. Check the list of databases
3. Look for `ridehailing` database

### Option 3: Quick PowerShell/CMD Check

```powershell
psql -U postgres -c "\l" | Select-String "ridehailing"
```

## Create the Database

### Using psql:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create the database
CREATE DATABASE ridehailing;

# Verify creation
\l

# Connect to the new database
\c ridehailing

# Exit
\q
```

### Using createdb command:

```bash
createdb -U postgres ridehailing
```

## Next Steps After Creating Database

1. **Create tables** - Run the SQL scripts in this directory
2. **Or use Hibernate auto-create** (for development only):
   - Change `spring.jpa.hibernate.ddl-auto=none` to `spring.jpa.hibernate.ddl-auto=create`
   - Restart the Spring Boot app
   - Tables will be created automatically based on JPA entities

## Verify Database Connection from Application

Once the database is created, you can test the connection by:

1. **Starting the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Check the logs** - You should see successful database connection messages

3. **If connection fails**, check:
   - PostgreSQL is running
   - Database `ridehailing` exists
   - Username/password are correct in `application.properties`

## Database Schema

The complete schema design is documented in `DATABASE_SCHEMA.md` in the root directory.

## Migration Scripts

Migration scripts will be created in the `database/` directory for:
- Creating all tables
- Adding indexes
- Inserting initial data (roles, etc.)


