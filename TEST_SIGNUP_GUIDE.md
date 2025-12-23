# Quick Signup Testing Guide

## 🚀 Quick Start - Test Signup

Use these ready-to-use credentials to test the signup functionality:

### ✅ Recommended Test Account

**Copy and paste these into the app:**

```
Email: rider1@test.com
Password: Test123456
Phone: 1234567890
First Name: John
Last Name: Doe
```

### 📱 Step-by-Step Testing

1. **Open Flutter App**
   - Navigate to "Create Account" screen

2. **Fill in the form:**
   ```
   Registration Type: Rider (selected)
   First Name: John
   Last Name: Doe
   Email: rider1@test.com
   Phone Number: 1234567890
   Password: Test123456
   Confirm Password: Test123456
   ```

3. **Tap "Create Account"**

4. **Expected Result:**
   - ✅ Success message appears
   - ✅ User is logged in automatically
   - ✅ Redirected to home screen

### 🔍 Verify Registration Worked

After registration, check the database:

```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -c "SELECT email, first_name, last_name, status, created_at FROM users WHERE email = 'rider1@test.com';"
$env:PGPASSWORD=$null
```

You should see:
- Email: `rider1@test.com`
- First Name: `John`
- Last Name: `Doe`
- Status: `ACTIVE`
- Created timestamp

### 🧪 More Test Accounts

Try registering multiple users:

**Account 2:**
```
Email: jane@test.com
Password: Test123456
Phone: 9876543210
First Name: Jane
Last Name: Smith
```

**Account 3:**
```
Email: alice@test.com
Password: Password123
Phone: 5551234567
First Name: Alice
Last Name: Johnson
```

### ❌ Test Error Cases

**Test 1: Duplicate Email**
- Register with `rider1@test.com`
- Try to register again with same email
- Should get error: "Email already registered"

**Test 2: Missing Fields**
- Leave email or password empty
- Should get error: "Missing required fields"

**Test 3: Login After Registration**
- Register a new account
- Logout
- Login with same credentials
- Should successfully authenticate

### 🔐 Test Login

After registration, test login:

```
Email: rider1@test.com
Password: Test123456
```

Should successfully log in and show home screen.

### 📊 Check Database

View all registered users:

```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -c "SELECT user_id, email, first_name, last_name, status, created_at FROM users ORDER BY created_at DESC LIMIT 10;"
$env:PGPASSWORD=$null
```

### 🧹 Clean Up Test Data

To remove test users:

```sql
DELETE FROM users WHERE email LIKE '%@test.com';
```

Or via PowerShell:
```powershell
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -c "DELETE FROM users WHERE email LIKE '%@test.com';"
$env:PGPASSWORD=$null
```

## 🎯 Quick Copy-Paste Test Data

**Most Common Test:**
```
Email: test@test.com
Password: test123456
Phone: 1234567890
First Name: Test
Last Name: User
```

**For Multiple Tests:**
```
User 1: test1@test.com / test123456 / 1111111111 / Test / One
User 2: test2@test.com / test123456 / 2222222222 / Test / Two
User 3: test3@test.com / test123456 / 3333333333 / Test / Three
```

## ✅ Success Indicators

When registration works correctly, you should see:
1. ✅ Success dialog with user ID
2. ✅ Auto-login occurs
3. ✅ Home screen shows welcome message
4. ✅ Database contains the new user record
5. ✅ Password is hashed (not plain text) in database

## 🐛 Troubleshooting

**If registration fails:**
1. Check backend is running: `http://localhost:8080/health`
2. Check database connection
3. Check backend logs for errors
4. Verify tables exist: `\dt` in psql

**If login fails after registration:**
1. Verify user exists in database
2. Check password was hashed correctly
3. Try logging in with exact same credentials

