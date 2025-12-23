# Connecting Rider App to Backend via USB

When using a physical Android device connected via USB, you have **two options** to connect to your backend running on your computer.

## Option 1: Use Your Computer's IP Address (Recommended)

### Step 1: Find Your Computer's IP Address

**On Windows (PowerShell):**
```powershell
ipconfig | findstr /i "IPv4"
```

Look for the IP address on your local network (usually starts with `192.168.x.x` or `10.x.x.x`).

**Example output:**
```
IPv4 Address. . . . . . . . . . . : 192.168.0.125
```

### Step 2: Update API Configuration

Edit `lib/config/api_config.dart` and change the `baseUrl`:

```dart
static const String baseUrl = 'http://192.168.0.125:8080'; // Use YOUR IP address
```

**Important:** 
- Make sure your phone and computer are on the **same Wi-Fi network**
- Replace `192.168.0.125` with your actual IP address

### Step 3: Enable USB Debugging

1. On your Android phone:
   - Go to **Settings** → **About Phone**
   - Tap **Build Number** 7 times to enable Developer Options
   - Go back to **Settings** → **Developer Options**
   - Enable **USB Debugging**

2. Connect your phone via USB

3. When prompted on your phone, allow USB debugging

4. Verify connection:
   ```powershell
   flutter devices
   ```
   You should see your device listed.

### Step 4: Run the App

```powershell
cd rider_app
flutter run
```

---

## Option 2: Use ADB Port Forwarding (Alternative)

This method forwards `localhost:8080` from your device to your computer.

### Step 1: Enable USB Debugging

Same as Option 1, Step 3 above.

### Step 2: Set Up Port Forwarding

```powershell
adb reverse tcp:8080 tcp:8080
```

This forwards port 8080 from your device to your computer.

### Step 3: Update API Configuration

Edit `lib/config/api_config.dart`:

```dart
static const String baseUrl = 'http://localhost:8080';
```

### Step 4: Run the App

```powershell
cd rider_app
flutter run
```

**Note:** You need to run `adb reverse` every time you reconnect your device.

---

## Quick Reference

| Connection Method | baseUrl in `api_config.dart` | Requirements |
|-------------------|------------------------------|--------------|
| **Android Emulator** | `http://10.0.2.2:8080` | Emulator running |
| **USB Device (Network)** | `http://192.168.0.125:8080` | Same Wi-Fi network, use your IP |
| **USB Device (ADB)** | `http://localhost:8080` | Run `adb reverse tcp:8080 tcp:8080` first |

## Troubleshooting

### "Connection refused" or "Failed to connect"
- Make sure your backend is running on port 8080
- Check firewall settings (Windows Firewall might be blocking connections)
- Verify the IP address is correct
- For network method: Ensure phone and computer are on same Wi-Fi

### Device not detected
- Enable USB Debugging in Developer Options
- Allow USB debugging when prompted on phone
- Try different USB cable/port
- Run `adb devices` to check if device is recognized

### Backend not accessible
- Test backend in browser: `http://localhost:8080/health` (should work on computer)
- For network method: Test from phone's browser: `http://192.168.0.125:8080/health`
- Check if backend is bound to `0.0.0.0` (not just `localhost`) in Spring Boot config

