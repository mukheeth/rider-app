# Fix: Login Not Working on Physical Device

## ⚠️ Problem

Login fails on physical device because the app can't reach the backend server.

**Reason:** The API URL `http://10.0.2.2:8080` only works for **emulators**, not physical devices.

---

## ✅ **Solution: ADB Port Forwarding (Easiest)**

### **Step 1: Set Up Port Forwarding**

Run this command **once** (keeps working until you disconnect):

```powershell
adb reverse tcp:8080 tcp:8080
```

**Output:** Should show no error (means it worked!)

### **Step 2: Verify Backend is Running**

Make sure your Spring Boot backend is running on port 8080:

```powershell
# Check if backend is running
# You should see Spring Boot logs in your backend terminal
```

### **Step 3: Test Login**

Now try logging in on your physical device - it should work! ✅

---

## 🔄 **Alternative: Use Computer's IP Address**

If ADB forwarding doesn't work:

### **Step 1: Find Your Computer's IP**

```powershell
ipconfig
```

Look for **"IPv4 Address"** under your active network adapter (usually Wi-Fi or Ethernet).

Example: `192.168.0.125`

### **Step 2: Update API Config**

Edit `rider_app/lib/config/api_config.dart`:

```dart
static const String baseUrl = 'http://192.168.0.125:8080'; // Replace with YOUR IP
```

### **Step 3: Make Sure Phone and Computer Are on Same Network**

- Both must be on the **same Wi-Fi network**
- Or use USB tethering

### **Step 4: Test Login**

Try logging in again.

---

## 🎯 **Quick Fix (Recommended)**

**Just run this command:**

```powershell
adb reverse tcp:8080 tcp:8080
```

Then **restart the app** on your phone:

```powershell
flutter run -d 8d39956
```

**Login should work now!** ✅

---

## 🔍 **Verify It's Working**

### **Test Backend Connection:**

```powershell
# From your computer, test if backend is accessible
curl http://localhost:8080/health
```

### **Check ADB Forwarding:**

```powershell
adb reverse --list
```

Should show: `tcp:8080 tcp:8080`

---

## 📱 **What I Updated**

I've updated `api_config.dart` to use `localhost:8080` which works with ADB forwarding.

**After running `adb reverse tcp:8080 tcp:8080`, the app will connect to your backend!**

---

## ✅ **Steps to Fix:**

1. **Run:** `adb reverse tcp:8080 tcp:8080`
2. **Make sure backend is running** (check your Spring Boot terminal)
3. **Restart app:** `flutter run -d 8d39956`
4. **Try login** - should work now! 🎉

---

## 💡 **Pro Tip:**

**ADB forwarding persists** until you:
- Disconnect the USB cable
- Restart ADB
- Restart your computer

So you only need to run it **once per USB connection**!

---

## ✅ **Try Now:**

```powershell
adb reverse tcp:8080 tcp:8080
flutter run -d 8d39956
```

Then try logging in! 📱

