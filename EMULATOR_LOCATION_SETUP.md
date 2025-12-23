# Setting Up Location in Android Emulator

## Quick Fix: The app now uses a fallback location!

The app will automatically use a default location (New York City) if it can't get your real location. This means the map will work even without setting up emulator location!

---

## Option 1: Use Default Location (Easiest) ✅

**The app now does this automatically!** If location times out, it uses:
- **Default Location**: New York City (40.7128, -74.0060)
- **Map will show**: You'll see the map centered on NYC
- **No setup needed**: Just run the app!

---

## Option 2: Set Mock Location in Emulator

If you want to use a specific location:

### Method 1: Using Emulator Extended Controls

1. **Open Extended Controls:**
   - Click the "..." (three dots) button in the emulator toolbar
   - Or press `Ctrl + Shift + E`

2. **Go to Location:**
   - Click "Location" in the left sidebar
   - You'll see a map with a pin

3. **Set Location:**
   - **Option A**: Click on the map to set a location
   - **Option B**: Enter coordinates manually:
     - Latitude: `40.7128` (or any value)
     - Longitude: `-74.0060` (or any value)
   - Click "Set Location"

4. **Test:**
   - The app should now get this location
   - You can move the pin to simulate movement

### Method 2: Using ADB Command

```powershell
# Set location to New York City
adb emu geo fix -74.0060 40.7128

# Set location to San Francisco
adb emu geo fix -122.4194 37.7749

# Set location to London
adb emu geo fix -0.1276 51.5074

# Set location to your current city (find coordinates on Google Maps)
adb emu geo fix <longitude> <latitude>
```

**Note:** Coordinates are in format: `longitude latitude` (note the order!)

### Method 3: Using GPS Test App

1. Install a GPS test app from Play Store
2. Set mock location in the app
3. Your app will receive that location

---

## Option 3: Enable Location Services

1. **Open Settings** in emulator
2. Go to **Location** (or **Security & Location**)
3. Turn on **Location** toggle
4. Set mode to **High accuracy**

---

## Testing Location Updates

### Simulate Movement:

```powershell
# Move location slightly (simulate walking)
adb emu geo fix -74.0061 40.7129

# Move more (simulate driving)
adb emu geo fix -74.0070 40.7135
```

### Continuous Movement Script:

Create a file `simulate_movement.ps1`:

```powershell
# Simulate driver moving
$baseLon = -74.0060
$baseLat = 40.7128

for ($i = 0; $i -lt 10; $i++) {
    $lon = $baseLon + ($i * 0.001)
    $lat = $baseLat + ($i * 0.001)
    adb emu geo fix $lon $lat
    Write-Host "Location: $lat, $lon"
    Start-Sleep -Seconds 2
}
```

Run it:
```powershell
.\simulate_movement.ps1
```

---

## Common Locations for Testing

```powershell
# New York City
adb emu geo fix -74.0060 40.7128

# San Francisco
adb emu geo fix -122.4194 37.7749

# London
adb emu geo fix -0.1276 51.5074

# Tokyo
adb emu geo fix 139.6917 35.6895

# Sydney
adb emu geo fix 151.2093 -33.8688

# Mumbai
adb emu geo fix 72.8777 19.0760
```

---

## Troubleshooting

### Issue: Location still not working

1. **Check Location Services:**
   ```powershell
   adb shell settings get secure location_providers_allowed
   ```
   Should show: `gps,network`

2. **Enable Location:**
   ```powershell
   adb shell settings put secure location_providers_allowed +gps
   adb shell settings put secure location_providers_allowed +network
   ```

3. **Restart Emulator:**
   - Sometimes a restart helps

### Issue: App shows timeout error

**Solution:** The app now handles this automatically! It will:
- Show a friendly message
- Use default location (NYC)
- Map will still work
- You can click "Retry" to try again

### Issue: Want to use real device location

1. **Use Physical Device:**
   ```powershell
   flutter run -d <device-id>
   ```

2. **Enable Location on Device:**
   - Settings → Location → On
   - Grant permission to app

---

## What Changed

I've updated the app to:
- ✅ **Use fallback location** if real location fails
- ✅ **Longer timeout** (30 seconds instead of 10)
- ✅ **Lower accuracy** (medium instead of high) for faster response
- ✅ **Better error messages** with retry option
- ✅ **Map works even without location**

---

## Quick Test

1. **Run the app:**
   ```powershell
   flutter run
   ```

2. **If you see timeout:**
   - The app will automatically use NYC as default
   - Map will show
   - Click "Retry" if you set up emulator location

3. **Set emulator location (optional):**
   ```powershell
   adb emu geo fix -74.0060 40.7128
   ```

4. **Click "Retry" in the app**

The app should now work perfectly! 🎉

