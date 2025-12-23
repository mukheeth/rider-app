# Fix Android Emulator Connection to Backend

## Problem
The Flutter app on Android emulator can't connect to backend at `10.0.2.2:8080` even though backend is running.

## Solution Options

### Option 1: Allow Firewall Rule (Recommended)

1. **Open Windows Defender Firewall:**
   - Press `Win + R`
   - Type: `wf.msc` and press Enter

2. **Create Inbound Rule:**
   - Click "Inbound Rules" → "New Rule..."
   - Select "Port" → Next
   - Select "TCP" and enter port: `8080` → Next
   - Select "Allow the connection" → Next
   - Check all profiles (Domain, Private, Public) → Next
   - Name: "Spring Boot Backend" → Finish

3. **Restart the backend** (if it's running)

4. **Test again** in Flutter app

### Option 2: Use ADB Port Forwarding (Alternative)

This bypasses firewall issues by forwarding emulator's localhost to your computer.

1. **Stop the backend** (if running)

2. **Set up port forwarding:**
   ```powershell
   adb reverse tcp:8080 tcp:8080
   ```

3. **Update Flutter config:**
   - Edit `rider_app/lib/config/api_config.dart`
   - Change to: `static const String baseUrl = 'http://localhost:8080';`

4. **Start backend:**
   ```powershell
   cd C:\New_uber
   mvn spring-boot:run
   ```

5. **Restart Flutter app**

**Note:** You need to run `adb reverse` every time you reconnect the emulator.

### Option 3: Disable Firewall Temporarily (For Testing Only)

⚠️ **Not recommended for production!**

1. Open Windows Security
2. Firewall & network protection
3. Temporarily disable firewall for Private network
4. Test the connection
5. **Re-enable firewall after testing**

## Verify Backend is Accessible

Test from your computer:
```powershell
curl http://localhost:8080/health
```

Should return: `{"status":"UP"}`

## After Fixing

1. Restart the backend (if you changed `application.properties`)
2. Hot restart Flutter app (press `R` in Flutter terminal)
3. The app should now connect successfully



