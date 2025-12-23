# Troubleshooting App Crash on Startup

## Error: "Error waiting for a debug connection: The log reader stopped unexpectedly"

This error usually means the app crashed immediately after launch. I've added better error handling, but here are steps to debug:

---

## Quick Fixes

### 1. Check Logs for Actual Error

Run with verbose logging:
```powershell
flutter run -v
```

This will show the actual error that's causing the crash.

### 2. Check if Emulator is Running Properly

```powershell
flutter devices
```

Make sure your emulator is listed and shows as "available".

### 3. Clean and Rebuild

```powershell
cd rider_app
flutter clean
flutter pub get
flutter run
```

---

## Common Causes & Solutions

### Issue 1: Location Permission Crash

**Symptoms:** App crashes immediately on startup

**Solution:** The app now handles this gracefully, but if it still crashes:
- Make sure you're testing on Android 6.0+ (runtime permissions)
- The app will request permission when needed

### Issue 2: WebSocket Connection Failure

**Symptoms:** App might hang or crash when trying to connect

**Solution:** 
- Make sure backend is running
- The app now handles WebSocket failures gracefully
- It will continue without WebSocket if connection fails

### Issue 3: Missing Dependencies

**Symptoms:** Build errors or runtime crashes

**Solution:**
```powershell
cd rider_app
flutter pub get
flutter clean
flutter run
```

### Issue 4: Emulator Issues

**Symptoms:** App installs but crashes immediately

**Solution:**
1. Restart emulator
2. Check emulator has internet connection
3. Try a different emulator or physical device

---

## Debug Steps

### Step 1: Check Flutter Doctor

```powershell
flutter doctor -v
```

Fix any issues shown.

### Step 2: Check Android Logs

```powershell
adb logcat | findstr flutter
```

Or view full logs:
```powershell
adb logcat
```

Look for error messages or stack traces.

### Step 3: Run in Debug Mode

```powershell
flutter run --debug
```

This provides more detailed error information.

### Step 4: Check if App Installs

```powershell
flutter install
```

If this fails, there's a build issue.

---

## What I Fixed

I've added the following improvements to prevent crashes:

1. **Delayed Initialization** - Location tracking starts after widget is fully built
2. **Timeout Handling** - All async operations have timeouts
3. **Error Handling** - All errors are caught and logged, not thrown
4. **Mounted Checks** - All setState calls check if widget is still mounted
5. **Graceful Degradation** - App works even if location/WebSocket fails

---

## Test Without Location

If location is causing issues, you can temporarily disable it:

1. Open `home_screen.dart`
2. Comment out `_initializeLocationTracking()` in `initState`
3. The map will still show, just without your location

---

## Still Having Issues?

1. **Check the actual error:**
   ```powershell
   flutter run -v > error_log.txt
   ```
   Then check `error_log.txt` for the real error.

2. **Try on a physical device** instead of emulator

3. **Check backend is running** - The app tries to connect to WebSocket on startup

4. **Share the error log** - Run `flutter run -v` and share the output

---

## Expected Behavior Now

With the fixes:
- ✅ App should start even if location permission is denied
- ✅ App should start even if WebSocket connection fails
- ✅ App should show map even without location
- ✅ Errors are logged, not thrown (won't crash)

The app should now start successfully! 🎉

