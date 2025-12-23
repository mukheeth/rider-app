# Quick Test Data - Emails & Passwords

## Ready-to-Use Test Accounts

### Test Account 1
```
Email: user1@test.com
Password: Test123456
Phone: 1111111111
First Name: User
Last Name: One
```

### Test Account 2
```
Email: user2@test.com
Password: SecurePass789
Phone: 2222222222
First Name: User
Last Name: Two
```

### Test Account 3
```
Email: user3@test.com
Password: MyPassword123!
Phone: 3333333333
First Name: User
Last Name: Three
```

### Test Account 4
```
Email: user4@test.com
Password: Complex@Pass#2024
Phone: 4444444444
First Name: User
Last Name: Four
```

### Test Account 5
```
Email: user5@test.com
Password: SimplePass
Phone: 5555555555
First Name: User
Last Name: Five
```

## Quick Test Commands

### Test Single User:
```powershell
.\test_registration_custom.ps1 -Email "user1@test.com" -Password "Test123456" -FirstName "User" -LastName "One" -Phone "1111111111"
```

### Test Multiple Users (5 different passwords):
```powershell
.\test_multiple_users.ps1
```

### Test with Different Password:
```powershell
.\test_registration_custom.ps1 -Email "newuser@test.com" -Password "MySecurePass123!" -FirstName "New" -LastName "User" -Phone "9999999999"
```

## Password Variations to Test

**Simple:**
- `Test123456`
- `Password123`
- `SimplePass`

**With Special Characters:**
- `Test@123`
- `Pass#Word$2024`
- `My!Secure@Pass`

**Complex:**
- `Complex@Pass#2024`
- `VerySecure!123$Pass`
- `P@ssw0rd!Complex`

**Short:**
- `Pass1`
- `Test1`
- `User1`

**Long:**
- `VeryLongPassword123456789`
- `ThisIsAVeryLongPasswordForTesting123`

## Important: Restart Backend First!

⚠️ **Before testing, restart your backend:**

```powershell
# Stop backend (Ctrl+C)
# Then restart:
mvn clean compile
mvn spring-boot:run
```

## Verify Passwords Are Hashed

After registration, check that passwords are hashed (not plain text):

```sql
SELECT email, 
       password_hash,
       SUBSTRING(password_hash, 1, 7) as hash_type,
       LENGTH(password_hash) as hash_length
FROM users 
WHERE email LIKE '%@test.com';
```

**Expected:**
- `hash_type` should be `$2a$12` or `$2b$12` (BCrypt)
- `hash_length` should be 60 characters
- Never plain text!

## Test Login After Registration

Test that login works with the password you registered:

```powershell
$body = @{
    email='user1@test.com'
    password='Test123456'
} | ConvertTo-Json

Invoke-WebRequest -Uri 'http://localhost:8080/api/v1/auth/login' `
    -Method POST `
    -Body $body `
    -ContentType 'application/json'
```

