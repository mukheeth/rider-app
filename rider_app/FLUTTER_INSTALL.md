# How to Install Flutter SDK on Windows

## Step-by-Step Installation Guide

### Step 1: Download Flutter SDK

1. **Go to Flutter official website:**
   - Visit: https://docs.flutter.dev/get-started/install/windows
   - Or direct download: https://storage.googleapis.com/flutter_infra_release/releases/stable/windows/flutter_windows_3.24.0-stable.zip
   (Check the website for the latest version)

2. **Download the ZIP file:**
   - Click "Download Flutter SDK" button
   - The file is typically 1-2 GB in size
   - Wait for download to complete

### Step 2: Extract Flutter SDK

1. **Extract the ZIP file:**
   - Right-click the downloaded zip file
   - Select "Extract All..."
   - Choose a location like: `C:\src\flutter`
   - ⚠️ **Important:** Do NOT extract to:
     - Paths with spaces (like `C:\Program Files\`)
     - Paths with special characters
     - Protected folders that require admin rights
   - ✅ **Good locations:**
     - `C:\src\flutter`
     - `C:\development\flutter`
     - `D:\flutter`

2. **Verify extraction:**
   - Navigate to the extracted folder
   - You should see folders like: `bin`, `packages`, `examples`, etc.
   - The `flutter.exe` should be in the `bin` folder

### Step 3: Add Flutter to PATH

1. **Open Environment Variables:**
   - Press `Windows Key + R`
   - Type: `sysdm.cpl` and press Enter
   - OR search "Environment Variables" in Start menu
   - Click "Environment Variables" button

2. **Add Flutter to User PATH:**
   - Under "User variables" section, find "Path"
   - Click "Edit..."
   - Click "New"
   - Add the path to Flutter's `bin` folder:
     ```
     C:\src\flutter\bin
     ```
     (Replace with your actual Flutter path)
   - Click "OK" on all dialogs

3. **Verify PATH was added:**
   - Close ALL open terminal windows/PowerShell windows
   - Open a NEW PowerShell window
   - Type:
     ```powershell
     flutter --version
     ```
   - You should see Flutter version information

### Step 4: Run Flutter Doctor

1. **Check your setup:**
   ```powershell
   flutter doctor
   ```

2. **What to expect:**
   - ✅ Flutter (will show a checkmark if installed correctly)
   - ⚠️ Android toolchain (will show issues - this is normal)
   - ⚠️ Android Studio (will show "not installed" if not installed)
   - ⚠️ VS Code (optional)
   - ⚠️ Connected devices (none yet - this is normal)

### Step 5: Install Additional Tools (if needed)

Flutter doctor will tell you what's missing. Common next steps:

1. **Git (if not installed):**
   - Download from: https://git-scm.com/download/win
   - Install with default settings

2. **Android Studio** (for Android development):
   - See `SETUP_INSTRUCTIONS.md` for details
   - Or see `ALTERNATIVE_SETUP.md` for alternatives

## Quick Verification

After installation, verify everything works:

```powershell
# Check Flutter version
flutter --version

# Check Flutter installation
flutter doctor

# Check if you can create a project (optional test)
flutter --help
```

## Troubleshooting

### Problem: "flutter: command not found" after adding to PATH

**Solutions:**
1. **Restart terminal:**
   - Close ALL PowerShell/CMD windows
   - Open a NEW terminal window
   - Try `flutter --version` again

2. **Verify PATH:**
   ```powershell
   echo $env:PATH
   ```
   Look for your Flutter path in the output

3. **Check path is correct:**
   - Make sure you added `C:\src\flutter\bin` (with `\bin` at the end)
   - Not just `C:\src\flutter`

4. **Use full path to test:**
   ```powershell
   C:\src\flutter\bin\flutter.exe --version
   ```
   If this works, PATH is not set correctly

### Problem: Flutter doctor shows errors

This is **normal** at first! Common issues:
- ❌ Android toolchain - Install Android Studio (see SETUP_INSTRUCTIONS.md)
- ❌ Chrome - Optional, only needed for web development
- ❌ VS Code - Optional, only needed if using VS Code
- ✅ Flutter - Should show checkmark if installed correctly

### Problem: Download is slow

- Flutter SDK is ~1-2 GB
- Use a stable internet connection
- Try again if download fails
- Consider using a download manager

## Next Steps

Once Flutter SDK is installed:

1. ✅ Flutter SDK installed
2. ⏭️ Install Android Studio (or use alternative setup)
3. ⏭️ Set up Android emulator
4. ⏭️ Run the app: `cd rider_app && flutter pub get && flutter run`

## Summary

**Quick Install Commands:**

```powershell
# 1. Download Flutter SDK (do this manually from website)
# 2. Extract to C:\src\flutter (or your preferred location)
# 3. Add C:\src\flutter\bin to PATH (via Environment Variables)
# 4. Restart terminal
# 5. Verify:
flutter --version
flutter doctor
```

That's it! Flutter SDK installation is complete once `flutter --version` works.



