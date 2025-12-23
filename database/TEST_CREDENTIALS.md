# Test Credentials for Signup

Use these test credentials to test the registration and login functionality.

## Test User Accounts

### Test Rider 1
- **Email**: `rider1@test.com`
- **Password**: `Test123456`
- **Phone Number**: `1234567890`
- **First Name**: `John`
- **Last Name**: `Doe`

### Test Rider 2
- **Email**: `rider2@test.com`
- **Password**: `Test123456`
- **Phone Number**: `9876543210`
- **First Name**: `Jane`
- **Last Name**: `Smith`

### Test Rider 3
- **Email**: `alice@test.com`
- **Password**: `Password123`
- **Phone Number**: `5551234567`
- **First Name**: `Alice`
- **Last Name**: `Johnson`

### Test Rider 4
- **Email**: `bob@test.com`
- **Password**: `SecurePass456`
- **Phone Number**: `5559876543`
- **First Name**: `Bob`
- **Last Name**: `Williams`

## Quick Test Scenarios

### Scenario 1: Successful Registration
1. Use **Test Rider 1** credentials
2. Register via the app
3. Should get success message
4. User should be saved to database

### Scenario 2: Duplicate Email Test
1. Register with **Test Rider 1** (already registered)
2. Try to register again with same email
3. Should get error: "Email already registered"

### Scenario 3: Duplicate Phone Test
1. Register with **Test Rider 2**
2. Try to register with different email but same phone number
3. Should get error: "Phone number already registered"

### Scenario 4: Login Test
1. Register with **Test Rider 3**
2. Logout
3. Login with same credentials
4. Should successfully authenticate

### Scenario 5: Invalid Password Test
1. Register with **Test Rider 4**
2. Try to login with wrong password
3. Should get error: "Invalid email or password"

## Testing via Flutter App

### Step-by-Step:
1. Open the Flutter app
2. Navigate to "Create Account" screen
3. Fill in the form:
   - Select "Rider"
   - Enter First Name: `John`
   - Enter Last Name: `Doe`
   - Enter Email: `rider1@test.com`
   - Enter Phone: `1234567890`
   - Enter Password: `Test123456`
   - Confirm Password: `Test123456`
4. Tap "Create Account"
5. Should see success message
6. Auto-login should occur

## Testing via API (Postman/curl)

### Register Rider:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register/rider \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rider1@test.com",
    "password": "Test123456",
    "phoneNumber": "1234567890",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rider1@test.com",
    "password": "Test123456"
  }'
```

## Verify in Database

After registration, verify the user was saved:

```sql
-- Check all users
SELECT user_id, email, first_name, last_name, status, created_at 
FROM users 
ORDER BY created_at DESC;

-- Check specific user
SELECT u.user_id, u.email, u.first_name, u.last_name, u.status, 
       r.role_name, r.total_rides
FROM users u
JOIN roles r ON u.role_id = r.role_id
LEFT JOIN riders rid ON u.user_id = rid.rider_id
WHERE u.email = 'rider1@test.com';

-- Check password hash (should be BCrypt hash, not plain text)
SELECT email, password_hash 
FROM users 
WHERE email = 'rider1@test.com';
```

## Password Requirements

Current implementation accepts any password. For testing:
- Use simple passwords like: `Test123456`
- Use complex passwords like: `SecurePass123!@#`
- Both will work for testing

## Notes

- All test emails use `@test.com` domain for easy identification
- Phone numbers are 10 digits (US format)
- Passwords are hashed with BCrypt before storage
- Each user gets a unique UUID as `user_id`
- Registration automatically creates a `rider` record

## Clean Up Test Data

To remove test users:

```sql
-- Delete specific user (will cascade delete rider record)
DELETE FROM users WHERE email = 'rider1@test.com';

-- Delete all test users
DELETE FROM users WHERE email LIKE '%@test.com';

-- Reset riders table
TRUNCATE TABLE riders CASCADE;
TRUNCATE TABLE users CASCADE;
-- Note: Roles table should NOT be truncated as it contains system data
```

