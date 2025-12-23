# Set Location to Bangalore, India

## ✅ Quick Fix: Set Emulator to Bangalore

### Method 1: ADB Command (Easiest)

```powershell
adb emu geo fix 77.5946 12.9716
```

**Note:** Coordinates are in format: `longitude latitude` (note the order!)

### Method 2: Extended Controls

1. Click "..." (three dots) in emulator
2. Go to **Location**
3. Enter coordinates:
   - **Latitude:** `12.9716`
   - **Longitude:** `77.5946`
4. Click **Set Location**

### Method 3: Use Physical Device

If you're testing on a real Android device:
- The app will automatically use your real location
- No need to set coordinates manually

---

## 📍 Bangalore Coordinates

- **Latitude:** 12.9716
- **Longitude:** 77.5946
- **City:** Bangalore (Bengaluru), Karnataka, India

---

## 🗺️ Popular Bangalore Locations

### For Testing:

```powershell
# MG Road (City Center)
adb emu geo fix 77.6104 12.9750

# Indiranagar
adb emu geo fix 77.6408 12.9784

# Whitefield
adb emu geo fix 77.7498 12.9698

# Koramangala
adb emu geo fix 77.6273 12.9352

# Airport
adb emu geo fix 77.7062 13.1986
```

---

## ✅ What I Updated

1. **Fallback location** changed to Bangalore
2. **Default location** in book ride screen = Bangalore
3. **Emulator command** provided above

---

## 🚀 After Setting Location

1. **Hot reload the app** (press `r` in terminal)
2. **Or restart:**
   ```powershell
   flutter run
   ```

3. **Map should show Bangalore!** 🗺️

---

## 🧪 Test Movement in Bangalore

```powershell
# Start at MG Road
adb emu geo fix 77.6104 12.9750

# Move to Indiranagar (simulate driving)
adb emu geo fix 77.6408 12.9784

# Move to Koramangala
adb emu geo fix 77.6273 12.9352
```

Watch the map - blue marker should move! 📍

---

## 💡 Pro Tip

**For Real Device:**
- Just enable location on your phone
- App will use your actual Bangalore location automatically!

---

## ✅ Done!

Run the ADB command above, then hot reload the app. You should see Bangalore on the map! 🇮🇳

