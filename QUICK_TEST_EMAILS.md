# Quick Test Emails for Registration

Use these different email addresses to test registration:

## Test Email Options

### Option 1: Simple Test Email
```
Email: newuser@test.com
Password: Test123456
Phone: 5551234567
First Name: New
Last Name: User
```

### Option 2: Different Domain
```
Email: user@example.com
Password: Test123456
Phone: 5559876543
First Name: Example
Last Name: User
```

### Option 3: With Numbers
```
Email: user123@test.com
Password: Test123456
Phone: 5551112222
First Name: User
Last Name: OneTwoThree
```

### Option 4: Realistic Email
```
Email: john.doe@example.com
Password: Test123456
Phone: 5551234567
First Name: John
Last Name: Doe
```

### Option 5: Another Test
```
Email: jane.smith@test.com
Password: Test123456
Phone: 5559876543
First Name: Jane
Last Name: Smith
```

## Quick Test Commands

### Test with Custom Email:
```powershell
.\test_registration_custom.ps1 -Email "newuser@test.com" -FirstName "New" -LastName "User" -Phone "5551234567"
```

### Test with Different Email:
```powershell
.\test_registration_custom.ps1 -Email "user@example.com" -FirstName "Example" -LastName "User" -Phone "5559876543"
```

### Test with Auto-Generated Email (unique each time):
```powershell
.\test_registration.ps1
```

## Check All Registered Users

```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -c "SELECT email, first_name, last_name, status, created_at FROM users ORDER BY created_at DESC;"
$env:PGPASSWORD=$null
```

## Important Notes

⚠️ **Backend Must Be Restarted** after code changes!

If you see "null identifier" error:
1. Stop backend (Ctrl+C)
2. Restart: `mvn spring-boot:run`
3. Try registration again

