# Configure DNS in Android Emulator

## 🎯 You're in the Right Place!

You're looking at **Network & internet** settings. Here's how to fix DNS:

---

## ✅ Method 1: Private DNS (What You're Looking At)

1. **Tap "Private DNS"** (it shows "Off" currently)

2. **Select "Private DNS provider hostname"**

3. **Enter:** `dns.google`
   - This uses Google's DNS (8.8.8.8)

4. **Tap "Save"**

5. **Go back and restart emulator**

---

## ✅ Method 2: Via Extended Controls

1. **Click "..." (three dots)** in emulator toolbar

2. **Go to Settings → Network**

3. **Set DNS:**
   - Primary: `8.8.8.8`
   - Secondary: `8.8.4.4`

4. **Click Apply**

5. **Restart emulator**

---

## ✅ Method 3: Via ADB (Command Line)

```powershell
# Set Google DNS
adb shell "settings put global private_dns_mode hostname"
adb shell "settings put global private_dns_specifier dns.google"

# Or use IP directly
adb shell "setprop net.dns1 8.8.8.8"
adb shell "setprop net.dns2 8.8.4.4"

# Restart network
adb shell svc wifi disable
adb shell svc wifi enable
```

---

## 🧪 Test DNS After Configuration

```powershell
# Test if DNS works
adb shell ping -c 3 google.com

# Test OpenStreetMap
adb shell ping -c 3 tile.openstreetmap.org
```

If ping works, DNS is configured correctly!

---

## 🚀 After Configuring DNS

1. **Restart emulator** (important!)

2. **Run app:**
   ```powershell
   flutter run
   ```

3. **Map tiles should now load!** 🗺️

---

## 📝 Quick Steps Summary

1. Tap "Private DNS" → Select "Private DNS provider hostname"
2. Enter: `dns.google`
3. Save
4. Restart emulator
5. Run `flutter run`
6. Map should work! ✅

---

## 💡 Why This Fixes It

- Emulator's default DNS sometimes can't resolve `tile.openstreetmap.org`
- Google DNS (8.8.8.8) is more reliable
- This allows map tiles to load properly

Try Method 1 first (Private DNS) - it's the easiest! 🎯

