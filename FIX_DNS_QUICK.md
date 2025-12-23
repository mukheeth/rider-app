# Quick Fix: DNS/Network Issue in Emulator

## 🔧 The Problem

Emulator can't resolve `tile.openstreetmap.org` - this is a DNS/network configuration issue.

## ✅ Solution 1: Configure DNS in Emulator (Easiest)

1. **In Emulator:**
   - Click "..." (three dots) button
   - Go to **Settings** → **Network**
   - Set DNS to:
     - Primary: `8.8.8.8`
     - Secondary: `8.8.4.4`
   - Click **Apply**
   - **Restart Emulator**

2. **Run app again:**
   ```powershell
   flutter run
   ```

## ✅ Solution 2: Use ADB Commands

```powershell
# Set Google DNS
adb shell "settings put global private_dns_mode off"
adb shell "setprop net.dns1 8.8.8.8"
adb shell "setprop net.dns2 8.8.4.4"

# Restart network
adb shell svc wifi disable
adb shell svc wifi enable

# Test
adb shell ping -c 3 google.com
```

## ✅ Solution 3: Restart Everything

```powershell
# 1. Close emulator completely
# 2. Restart emulator
# 3. Run:
flutter run
```

## ✅ Solution 4: Use Physical Device

If emulator keeps having issues:
```powershell
# Connect phone via USB
# Enable USB debugging
flutter devices
flutter run -d <your-device-id>
```

## 📝 Note

The map will still work even if tiles don't load - you'll just see gray tiles. The app won't crash.

## 🎯 Best Solution

**Configure DNS in emulator settings (Solution 1)** - this fixes it permanently!

