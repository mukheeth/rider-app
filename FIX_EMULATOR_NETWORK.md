# Fix: Emulator Network/DNS Issues

## Problem: "Failed host lookup: 'tile.openstreetmap.org'"

The emulator can't resolve DNS for OpenStreetMap. This is a common emulator network issue.

---

## ✅ Quick Fixes

### Option 1: Configure Emulator DNS (Recommended)

1. **Open Emulator Settings:**
   - Click "..." (three dots) in emulator toolbar
   - Go to **Settings** → **Network**

2. **Set DNS:**
   - Primary DNS: `8.8.8.8` (Google DNS)
   - Secondary DNS: `8.8.4.4`
   - Click **Apply**

3. **Restart Emulator**

### Option 2: Use ADB to Set DNS

```powershell
# Set DNS to Google DNS
adb shell "settings put global private_dns_mode off"
adb shell "settings put global private_dns_specifier 8.8.8.8"
```

### Option 3: Restart Emulator Network

```powershell
# Restart network stack
adb shell svc wifi disable
adb shell svc wifi enable
```

### Option 4: Use Physical Device

If emulator keeps having network issues:
- Connect physical Android device
- Enable USB debugging
- Run: `flutter run -d <device-id>`

---

## 🔧 Alternative: Use Different Tile Provider

If OpenStreetMap doesn't work, we can switch to:
- CartoDB (free, no API key)
- Stamen (free, no API key)
- Mapbox (requires API key)

---

## ✅ What I Fixed

I've added:
- ✅ Error handling for tile loading
- ✅ Map will still work (shows gray tiles if network fails)
- ✅ App won't crash from tile errors

---

## 🚀 Try Again

After fixing DNS:

```powershell
flutter run
```

The map tiles should load now!

---

## 🧪 Test Network in Emulator

```powershell
# Test DNS resolution
adb shell ping -c 3 8.8.8.8

# Test internet
adb shell ping -c 3 google.com
```

If ping works, DNS is fine. If not, use Option 1 above.

---

## 💡 Pro Tip

**Best Solution:** Use Google DNS (8.8.8.8) in emulator settings. This fixes 90% of DNS issues!

