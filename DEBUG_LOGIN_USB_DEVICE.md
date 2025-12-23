# Debug: Login Issue on USB Device

## 🔍 **What to Check**

After entering credentials, if login fails, check these:

---

## ✅ **Step 1: Verify ADB Port Forwarding**

```powershell
adb -s 8d39956 reverse --list
```

**Should show:** `tcp:8080 tcp:8080`

**If not, run:**
```powershell
adb -s 8d39956 reverse tcp:8080 tcp:8080
```

---

## ✅ **Step 2: Check Backend is Running**

**In your backend terminal, you should see:**
- Spring Boot started
- Server running on port 8080
- No errors

**If backend is not running:**
1. Go to backend directory
2. Run: `mvn spring-boot:run`
3. Wait for "Started RideHailingApplication"

---

## ✅ **Step 3: Test Backend Connection**

**From your computer:**
```powershell
curl http://localhost:8080/health
```

**Should return:** `{"status":"ok"}` or similar

**If it fails:** Backend is not running or not accessible

---

## ✅ **Step 4: Check Error Message**

**In the app, after login fails, you'll see:**
- Red error banner at bottom
- Error message will tell you what's wrong

**Common errors:**
- `Connection timeout` → Backend not running or ADB forwarding not set
- `Network error` → Internet/network issue
- `Login failed: 401` → Wrong email/password
- `Login failed: 500` → Backend error

---

## 🔧 **What I Added**

I've improved error handling to show:
- ✅ **Clear error messages** with troubleshooting steps
- ✅ **Connection timeout handling** (10 seconds)
- ✅ **Better network error detection**
- ✅ **Helpful troubleshooting hints**

---

## 🚀 **Quick Fix Steps**

1. **Set up ADB forwarding:**
   ```powershell
   adb -s 8d39956 reverse tcp:8080 tcp:8080
   ```

2. **Make sure backend is running:**
   - Check backend terminal
   - Should show "Started RideHailingApplication"

3. **Test login again:**
   - Enter credentials
   - Check error message if it fails

---

## 📱 **What You'll See**

### **If ADB forwarding is missing:**
```
Connection timeout. Please check:
1. Backend is running
2. ADB forwarding: adb reverse tcp:8080 tcp:8080
3. Internet connection
```

### **If backend is not running:**
```
Connection error: Connection refused
Please check:
1. Backend is running on port 8080
2. Run: adb reverse tcp:8080 tcp:8080
3. Device and computer are connected
```

### **If wrong credentials:**
```
Login failed: 401
Invalid email or password
```

---

## ✅ **Try Now**

1. **Set ADB forwarding:**
   ```powershell
   adb -s 8d39956 reverse tcp:8080 tcp:8080
   ```

2. **Make sure backend is running**

3. **Try login again**

4. **Check the error message** - it will tell you what's wrong!

---

## 💡 **Pro Tip**

**Keep ADB forwarding active:**
- It stays active until you disconnect USB
- You only need to run it once per USB connection
- If you reconnect USB, run it again

---

## ✅ **Status**

**Better error messages are now in place!**

Try logging in and check the error message - it will guide you to the fix! 🔍

