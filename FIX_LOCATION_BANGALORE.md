# Fix: Location Not Updating to Bangalore

## ✅ I Just Set It Again

I've run the command to set emulator to Bangalore. Now you need to:

---

## 🔄 Step 1: Refresh Location in App

**In the app:**
1. **Tap the "My Location" button** (target icon in bottom-right of map)
   - This will force a fresh location update
   - Map will center on Bangalore

**OR**

2. **Pull down to refresh** (if available)
   - Or restart the app

---

## 🔧 Step 2: Verify Emulator Location

```powershell
# Check current emulator location
adb shell "dumpsys location | findstr Location"
```

**OR set it again:**
```powershell
adb emu geo fix 77.5946 12.9716
```

---

## 🗺️ Step 3: Test Different Bangalore Locations

```powershell
# MG Road (City Center)
adb emu geo fix 77.6104 12.9750

# Indiranagar
adb emu geo fix 77.6408 12.9784

# Koramangala
adb emu geo fix 77.6273 12.9352

# Whitefield
adb emu geo fix 77.7498 12.9698
```

After each command, **tap "My Location" button** in app to refresh.

---

## 🎯 Quick Fix: Restart App

```powershell
# Stop app (Ctrl+C)
flutter run
```

This will:
- Get fresh location from emulator
- Show Bangalore on map
- Update coordinates

---

## 📱 Alternative: Use Physical Device

If you have an Android phone in Bangalore:

1. **Connect phone via USB**
2. **Enable USB debugging**
3. **Run:**
   ```powershell
   flutter devices
   flutter run -d <your-device-id>
   ```

The app will automatically use your **real Bangalore location**! 🇮🇳

---

## ✅ What to Expect

After setting location and refreshing:
- ✅ Map shows Bangalore streets
- ✅ Coordinates: 12.9716, 77.5946
- ✅ Blue marker in Bangalore
- ✅ Location updates work

---

## 🔍 Troubleshooting

### If location still wrong:

1. **Check emulator GPS:**
   - Extended Controls → Location
   - Make sure GPS is enabled

2. **Restart emulator:**
   - Close completely
   - Reopen
   - Set location again

3. **Clear app data:**
   ```powershell
   adb shell pm clear com.ridehailing.rider_app
   ```

4. **Reinstall app:**
   ```powershell
   flutter clean
   flutter run
   ```

---

## 🚀 Try Now

1. **Tap "My Location" button** in app (bottom-right of map)
2. **Map should center on Bangalore!** 🗺️

If it still shows wrong location, restart the app after setting the emulator location.

