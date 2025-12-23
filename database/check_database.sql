-- Script to check if the ridehailing database exists
-- Run this in psql: psql -U postgres -f check_database.sql

-- Check if database exists
SELECT datname 
FROM pg_database 
WHERE datname = 'ridehailing';

-- If the above returns no rows, the database doesn't exist
-- Create it with: CREATE DATABASE ridehailing;

-- Once connected to the ridehailing database, check if tables exist
-- Connect to ridehailing: \c ridehailing
-- Then run:
-- \dt

-- To see all tables in the database:
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public'
ORDER BY table_name;


