# Database Migrations

This directory contains SQL migration scripts to set up the database schema.

## Running Migrations

### Option 1: Run all migrations at once

```powershell
cd database\migrations
psql -h localhost -p 5433 -U postgres -d ridehailing -f run_all_migrations.sql
```

### Option 2: Run migrations individually

```powershell
# Set password
$env:PGPASSWORD="root"

# Run each migration
psql -h localhost -p 5433 -U postgres -d ridehailing -f 001_create_roles_table.sql
psql -h localhost -p 5433 -U postgres -d ridehailing -f 002_create_users_table.sql
psql -h localhost -p 5433 -U postgres -d ridehailing -f 003_create_riders_table.sql
psql -h localhost -p 5433 -U postgres -d ridehailing -f 004_create_drivers_table.sql
psql -h localhost -p 5433 -U postgres -d ridehailing -f 005_create_vehicles_table.sql

# Clean up
$env:PGPASSWORD=$null
```

### Option 3: Using pgAdmin

1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Right-click on `ridehailing` database → Query Tool
4. Copy and paste the contents of each migration file in order
5. Execute each script

## Migration Order

1. **001_create_roles_table.sql** - Creates roles table (must run first)
2. **002_create_users_table.sql** - Creates users table (depends on roles)
3. **003_create_riders_table.sql** - Creates riders table (depends on users)
4. **004_create_drivers_table.sql** - Creates drivers table (depends on users)
5. **005_create_vehicles_table.sql** - Creates vehicles table (depends on drivers)
6. **006_create_rides_table.sql** - Creates rides table (depends on riders, drivers, vehicles)

## Verification

After running migrations, verify tables exist:

```sql
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;
```

Expected tables:
- roles
- users
- riders
- drivers
- vehicles
- rides

## Rollback

To drop all tables (use with caution):

```sql
DROP TABLE IF EXISTS rides CASCADE;
DROP TABLE IF EXISTS vehicles CASCADE;
DROP TABLE IF EXISTS drivers CASCADE;
DROP TABLE IF EXISTS riders CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;
```

