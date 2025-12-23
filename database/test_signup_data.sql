-- Test Signup Data
-- Use these SQL INSERT statements to directly create test users in the database
-- Note: Passwords are BCrypt hashed. Use AuthenticationService to create users properly.

-- Example: Check if test users exist
SELECT email, first_name, last_name, status 
FROM users 
WHERE email LIKE '%@test.com'
ORDER BY created_at DESC;

-- To create test users properly, use the registration API endpoint
-- This file is for reference only

-- Example test data structure:
-- Email: rider1@test.com
-- Password: Test123456 (hashed with BCrypt)
-- Phone: 1234567890
-- First Name: John
-- Last Name: Doe

-- To test registration via API:
-- POST http://localhost:8080/api/v1/auth/register/rider
-- {
--   "email": "rider1@test.com",
--   "password": "Test123456",
--   "phoneNumber": "1234567890",
--   "firstName": "John",
--   "lastName": "Doe"
-- }

