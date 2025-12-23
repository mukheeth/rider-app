# Fix: Install Failed on Physical Device

## ⚠️ Error: `INSTALL_FAILED_USER_RESTRICTED`

This means your phone is blocking the installation. Here's how to fix it:

---

## ✅ **Solution 1: Allow USB Installation (Most Common)**

### **On Your Phone:**

1. **When you see a popup** asking "Allow USB debugging?" → Tap **"Allow"** or **"OK"**

2. **Check for installation popup:**
   - Look for a popup asking "Install app?" or "Allow installation?"
   - Tap **"Install"** or **"Allow"**

3. **If no popup appears:**
   - Go to **Settings** → **Security** (or **Apps** → **Special access**)
   - Find **"Install unknown apps"** or **"USB installation"**
   - Enable it for **ADB** or **USB debugging**

---

## ✅ **Solution 2: Enable Developer Options**

### **On Your Phone:**

1. **Go to Settings** → **About phone**
2. **Tap "Build number" 7 times** (you'll see "You are now a developer!")
3. **Go back** → **System** → **Developer options**
4. **Enable:**
   - ✅ **USB debugging**
   - ✅ **Install via USB**
   - ✅ **USB debugging (Security settings)** (if available)

---

## ✅ **Solution 3: Disable Installation Restrictions**

### **On Your Phone:**

1. **Settings** → **Security** → **Install unknown apps**
2. **Find "ADB"** or **"USB debugging"**
3. **Enable "Allow from this source"**

**OR**

1. **Settings** → **Apps** → **Special access**
2. **Install unknown apps**
3. **Enable for ADB/USB**

---

## ✅ **Solution 4: Check USB Connection**

### **On Your Computer:**

```powershell
# Check if device is connected
adb devices

# If device shows "unauthorized", check your phone for popup
```

### **On Your Phone:**

- **Unlock your phone**
- **Look for popup:** "Allow USB debugging?"
- **Check "Always allow from this computer"**
- **Tap "Allow"**

---

## 🚀 **Quick Fix Steps:**

1. **Unlock your phone**
2. **Look for any popups** asking for permission
3. **Tap "Allow" or "Install"** on all popups
4. **Try again:**
   ```powershell
   flutter run -d 8d39956
   ```

---

## 📱 **Alternative: Use Emulator**

If you just want to test quickly:

```powershell
flutter run -d emulator-5554
```

The emulator doesn't have these restrictions!

---

## 🔍 **Verify Device Connection**

```powershell
# Check device status
adb devices

# Should show:
# 8d39956    device
```

If it shows **"unauthorized"**, check your phone for the USB debugging popup.

---

## ✅ **After Fixing:**

1. **Unlock phone**
2. **Allow all popups**
3. **Run:**
   ```powershell
   flutter run -d 8d39956
   ```

---

## 💡 **Pro Tip:**

**For physical device testing:**
- Keep phone **unlocked** during installation
- **Allow all popups** that appear
- **Enable "Always allow from this computer"** in USB debugging popup

---

## ✅ **Try Now:**

1. **Check your phone** for any popups
2. **Allow installation/USB debugging**
3. **Run again:**
   ```powershell
   flutter run -d 8d39956
   ```

The app should install and run on your physical device! 📱

