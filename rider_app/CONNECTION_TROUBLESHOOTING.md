# Connection Troubleshooting Guide

## ✅ Fixes Applied

1. **Added INTERNET Permission** - Required for network access
2. **Enabled Cleartext Traffic** - Allows HTTP (non-HTTPS) connections on Android 9+
3. **Backend Configuration** - Already configured to accept connections from all interfaces (`server.address=0.0.0.0`)

## 🔄 Next Steps

**Restart your Flutter app completely:**
1. Stop the app (press `q` in terminal where `flutter run` is running)
2. Run again: `flutter run`

## 🔍 If Still Not Working - Manual Checks

### 1. Verify Backend is Running
```powershell
# Check if backend is accessible from your browser
# Open: http://localhost:8080/health
# Should return: {"status":"UP"}
```

### 2. Test from Emulator's Browser
1. Open the emulator
2. Open Chrome browser in the emulator
3. Navigate to: `http://10.0.2.2:8080/health`
4. Should show: `{"status":"UP"}`

If this works in the emulator's browser but not in your app, there might be an app-specific issue.

### 3. Check Windows Firewall
The Windows Firewall might be blocking connections:
- Open Windows Defender Firewall
- Check if port 8080 is allowed
- If not, add an inbound rule for port 8080

### 4. Alternative: Use ADB Port Forwarding

If `10.0.2.2` still doesn't work, use ADB reverse:

1. **Set up port forwarding:**
   ```powershell
   adb reverse tcp:8080 tcp:8080
   ```

2. **Change API config back to localhost:**
   In `rider_app/lib/config/api_config.dart`, change:
   ```dart
   static const String baseUrl = 'http://localhost:8080';
   ```

3. **Restart the app**

### 5. Alternative: Use Your Computer's IP Address

1. **Find your computer's IP address:**
   ```powershell
   ipconfig
   ```
   Look for "IPv4 Address" under your active network adapter (usually something like `192.168.1.xxx` or `192.168.0.xxx`)

2. **Update API config:**
   In `rider_app/lib/config/api_config.dart`, change:
   ```dart
   static const String baseUrl = 'http://192.168.1.XXX:8080'; // Use YOUR IP address
   ```

3. **Ensure backend accepts connections from your network:**
   The backend is already configured with `server.address=0.0.0.0`, so this should work.

4. **Restart the app**

### 6. Check Backend Logs

When you try to connect from the app, check the backend console/logs. If you see connection attempts, the network is working but there might be an authentication or routing issue.

### 7. Verify AndroidManifest.xml

Make sure your `AndroidManifest.xml` has:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET"/>
    
    <application
        android:usesCleartextTraffic="true"
        ...>
        ...
    </application>
</manifest>
```

## 📝 Summary of Connection Methods

| Method | API Config | Pros | Cons |
|--------|-----------|------|------|
| **10.0.2.2 (Emulator)** | `http://10.0.2.2:8080` | Standard emulator method | Requires `usesCleartextTraffic="true"` |
| **ADB Reverse** | `http://localhost:8080` | Works like localhost | Must run `adb reverse` command each time |
| **Network IP** | `http://192.168.x.x:8080` | Works for both emulator and physical device | Need to find IP address |

## 🚨 Common Error Messages

- **"Operation not permitted"** → Missing INTERNET permission or `usesCleartextTraffic`
- **"Connection refused"** → Backend not running or not binding to 0.0.0.0
- **"Connection timed out"** → Firewall blocking or wrong IP address
- **"Network is unreachable"** → Wrong IP address or network issue

