# Troubleshooting "Lost Connection to Device"

## Quick Fixes

### 1. Restart the App

```powershell
# Stop current run (Ctrl+C if needed)
flutter run
```

### 2. Restart Emulator

1. Close the emulator
2. Open Android Studio → AVD Manager
3. Start emulator again
4. Run: `flutter run`

### 3. Check ADB Connection

```powershell
adb devices
```

Should show your device. If not:
```powershell
adb kill-server
adb start-server
adb devices
```

### 4. Clean and Rebuild

```powershell
cd rider_app
flutter clean
flutter pub get
flutter run
```

---

## Common Causes

### Cause 1: App Crashed
**Symptoms:** App closes suddenly, connection lost

**Solution:**
- Check logs for errors: `flutter run -v`
- Look for stack traces in output
- Check if specific action causes crash

### Cause 2: Memory Issue
**Symptoms:** App slows down, then disconnects

**Solution:**
- Restart emulator
- Increase emulator RAM in AVD settings
- Close other apps

### Cause 3: Emulator Freeze
**Symptoms:** Emulator stops responding

**Solution:**
- Restart emulator
- Use a different emulator
- Try physical device

### Cause 4: Network Issue
**Symptoms:** WebSocket errors, then disconnect

**Solution:**
- Check backend is running
- Check network connection
- Restart backend

---

## Debug Steps

### Step 1: Check Logs

```powershell
flutter run -v > app_log.txt
```

Then check `app_log.txt` for errors.

### Step 2: Check Android Logs

```powershell
adb logcat | findstr flutter
```

Or full logs:
```powershell
adb logcat > android_log.txt
```

### Step 3: Run in Release Mode

```powershell
flutter run --release
```

Release mode is more stable.

### Step 4: Check for Specific Errors

Look for:
- `NullPointerException`
- `StateError`
- `FormatException`
- Memory warnings

---

## Prevention (Code Fixes Applied)

I've added:
- ✅ Null checks before setState
- ✅ Mounted checks
- ✅ Try-catch blocks
- ✅ Error handling in navigation
- ✅ Safe async operations

---

## Quick Recovery

### If App Crashes on Startup:

1. **Check permissions:**
   - Location permission granted?
   - Internet permission?

2. **Check backend:**
   - Is backend running?
   - Can you access `http://10.0.2.2:8080/health`?

3. **Check dependencies:**
   ```powershell
   flutter pub get
   ```

### If App Crashes on Specific Action:

1. **Map selection:**
   - Check if location permission granted
   - Try without map selection first

2. **Ride booking:**
   - Check if rides table exists
   - Check backend logs

3. **Location tracking:**
   - Check GPS enabled
   - Check location permission

---

## Alternative: Use Physical Device

If emulator keeps disconnecting:

1. **Enable USB Debugging** on phone
2. **Connect via USB**
3. **Run:**
   ```powershell
   flutter devices
   flutter run -d <device-id>
   ```

---

## Still Having Issues?

1. **Share the full error log:**
   ```powershell
   flutter run -v 2>&1 | tee error_log.txt
   ```

2. **Check backend logs** for errors

3. **Try minimal test:**
   - Comment out location tracking
   - Comment out WebSocket
   - See if app runs

---

## Most Common Fix

**90% of the time, this works:**

```powershell
# 1. Stop everything
# 2. Restart emulator
# 3. Clean build
cd rider_app
flutter clean
flutter pub get
flutter run
```

The app should reconnect! 🔄

