-- Run all migrations in order
-- Execute this script to set up the database schema

\echo 'Creating roles table...'
\i 001_create_roles_table.sql

\echo 'Creating users table...'
\i 002_create_users_table.sql

\echo 'Creating riders table...'
\i 003_create_riders_table.sql

\echo 'Creating drivers table...'
\i 004_create_drivers_table.sql

\echo 'Creating vehicles table...'
\i 005_create_vehicles_table.sql

\echo 'Creating rides table...'
\i 006_create_rides_table.sql

\echo 'All migrations completed successfully!'
\echo 'Verifying tables...'
\dt

