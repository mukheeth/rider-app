# Test Passwords for Registration

Use these different password combinations to test registration:

## Test Password Options

### Simple Passwords
```
Password: Test123456
Password: Password123
Password: SimplePass
Password: test123456
```

### Medium Complexity
```
Password: SecurePass789
Password: MyPassword123!
Password: User@Pass2024
Password: Test#Password
```

### Complex Passwords
```
Password: Complex@Pass#2024
Password: VerySecure!123$Pass
Password: P@ssw0rd!Complex
Password: Test123!@#$%^&*
```

### Special Characters
```
Password: Test@123
Password: Pass#Word$2024
Password: My!Secure@Pass
Password: Test&Pass*2024
```

## Quick Test Commands

### Test with Custom Email and Password:
```powershell
.\test_registration_custom.ps1 -Email "user1@test.com" -Password "Test123456" -FirstName "User" -LastName "One" -Phone "1111111111"
```

### Test with Different Password:
```powershell
.\test_registration_custom.ps1 -Email "user2@test.com" -Password "SecurePass789" -FirstName "User" -LastName "Two" -Phone "2222222222"
```

### Test with Complex Password:
```powershell
.\test_registration_custom.ps1 -Email "user3@test.com" -Password "Complex@Pass#2024" -FirstName "User" -LastName "Three" -Phone "3333333333"
```

### Test Multiple Users at Once:
```powershell
.\test_multiple_users.ps1
```
(This will test 5 different users with different passwords)

## Test Scenarios

### Scenario 1: Simple Password
```
Email: simple@test.com
Password: Test123456
Phone: 1111111111
Name: Simple User
```

### Scenario 2: Password with Special Characters
```
Email: special@test.com
Password: Test@123#Pass
Phone: 2222222222
Name: Special User
```

### Scenario 3: Long Password
```
Email: longpass@test.com
Password: VeryLongPassword123456789
Phone: 3333333333
Name: Long Pass User
```

### Scenario 4: Short Password
```
Email: short@test.com
Password: Short1
Phone: 4444444444
Name: Short User
```

### Scenario 5: Password with Spaces (should fail validation)
```
Email: space@test.com
Password: Test 123456
Phone: 5555555555
Name: Space User
```

## Password Storage Verification

After registration, check how passwords are stored:

```sql
-- Check password hash (should be BCrypt hash, not plain text)
SELECT email, password_hash, 
       LENGTH(password_hash) as hash_length,
       SUBSTRING(password_hash, 1, 7) as hash_prefix
FROM users 
WHERE email LIKE '%@test.com'
ORDER BY created_at DESC;
```

**Expected:**
- `password_hash` should start with `$2a$` or `$2b$` (BCrypt format)
- Hash length should be 60 characters
- Never plain text passwords

## Test Login After Registration

After registering, test login with the same password:

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

## Password Requirements

**Current Implementation:** Accepts any password (no validation)

**For Testing:**
- ✅ Simple: `Test123456`
- ✅ Complex: `Complex@Pass#2024`
- ✅ Short: `Pass1`
- ✅ Long: `VeryLongPassword123456789`
- ✅ Special chars: `Test@123#Pass`

All passwords will be hashed with BCrypt before storage.

## Quick Copy-Paste Test Data

**User 1:**
```
Email: test1@test.com
Password: Test123456
Phone: 1111111111
```

**User 2:**
```
Email: test2@test.com
Password: SecurePass789
Phone: 2222222222
```

**User 3:**
```
Email: test3@test.com
Password: MyPassword123!
Phone: 3333333333
```

**User 4:**
```
Email: test4@test.com
Password: Complex@Pass#2024
Phone: 4444444444
```

**User 5:**
```
Email: test5@test.com
Password: SimplePass
Phone: 5555555555
```

