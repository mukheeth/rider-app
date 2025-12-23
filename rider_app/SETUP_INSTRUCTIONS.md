# Flutter Rider App - Setup Instructions

> **Note:** 
> - Android Studio is not mandatory! See [ALTERNATIVE_SETUP.md](ALTERNATIVE_SETUP.md) for options using VS Code + Command Line Tools, physical devices, or other emulators.
> - This guide focuses on **Android development first**. iOS support will be configured later using the same codebase.

## Prerequisites Installation

### Development Plan
1. ✅ **Phase 1: Android** (Current focus)
2. ⏳ **Phase 2: iOS** (After Android is complete)

Flutter is cross-platform, so the same code will work on both platforms once iOS configuration is added.

### Step 1: Install Flutter SDK

1. **Download Flutter SDK:**
   - Go to https://docs.flutter.dev/get-started/install/windows
   - Download the latest stable Flutter SDK (zip file)
   - Extract the zip file to a location like `C:\src\flutter` (avoid paths with spaces or special characters)

2. **Add Flutter to PATH:**
   - Open "Environment Variables" (search in Start menu)
   - Under "User variables", find "Path" and click "Edit"
   - Click "New" and add the path: `C:\src\flutter\bin` (or wherever you extracted Flutter)
   - Click "OK" on all dialogs
   - **Important:** Close and reopen your terminal/PowerShell for changes to take effect

3. **Verify Flutter Installation:**
   ```powershell
   flutter doctor
   ```
   This will check your setup and show what's missing.

### Step 2: Install Android Studio

1. **Download Android Studio:**
   - Go to https://developer.android.com/studio
   - Download Android Studio for Windows
   - Run the installer and follow the setup wizard

2. **Install Android SDK:**
   - Open Android Studio
   - Go to "More Actions" → "SDK Manager"
   - In "SDK Platforms" tab, check:
     - Android 14.0 (API 34) or latest stable
   - In "SDK Tools" tab, ensure these are checked:
     - Android SDK Build-Tools
     - Android SDK Command-line Tools
     - Android SDK Platform-Tools
     - Android Emulator
     - Intel x86 Emulator Accelerator (HAXM installer) - if you have Intel CPU
   - Click "Apply" and wait for installation

3. **Accept Android Licenses:**
   ```powershell
   flutter doctor --android-licenses
   ```
   Type `y` for each license agreement

### Step 3: Set Up Android Emulator

1. **Create an Android Virtual Device (AVD):**
   - Open Android Studio
   - Go to "More Actions" → "Virtual Device Manager"
   - Click "Create Device"
   - Select a device (e.g., "Pixel 6" or "Pixel 7")
   - Click "Next"
   - Select a system image (e.g., "Tiramisu" API 33 or "UpsideDownCake" API 34)
   - If not downloaded, click "Download" next to the system image
   - Click "Next"
   - Review the AVD configuration and click "Finish"

2. **Start the Emulator:**
   - In Virtual Device Manager, click the "Play" button next to your created device
   - Wait for the emulator to boot (first boot may take a few minutes)

### Step 4: Verify Setup

Run Flutter doctor to verify everything is set up correctly:

```powershell
flutter doctor
```

You should see checkmarks (✓) for:
- Flutter
- Android toolchain
- Android Studio
- Android license status

### Step 5: Install Dependencies and Run the App

1. **Navigate to the app directory:**
   ```powershell
   cd rider_app
   ```

2. **Get Flutter dependencies:**
   ```powershell
   flutter pub get
   ```

3. **Check available devices:**
   ```powershell
   flutter devices
   ```
   You should see your emulator listed

4. **Run the app:**
   ```powershell
   flutter run
   ```

   Or run on a specific device:
   ```powershell
   flutter run -d <device-id>
   ```

## Troubleshooting

### Flutter command not found
- Make sure you added Flutter to PATH correctly
- Restart your terminal/PowerShell after adding to PATH
- Verify with: `echo $env:PATH` (should include flutter\bin)

### Android licenses not accepted
```powershell
flutter doctor --android-licenses
```
Accept all licenses by typing `y`

### No devices found
- Make sure Android Emulator is running
- In Android Studio: Tools → Device Manager → Start your emulator
- Run `flutter devices` to verify the emulator is detected

### Gradle build errors
- First run may take longer as it downloads Gradle
- Ensure you have internet connection
- Try: `cd android && ./gradlew clean` (if on Windows, use `gradlew.bat clean`)

## Quick Start (Once Setup is Complete)

```powershell
# Navigate to app directory
cd rider_app

# Get dependencies
flutter pub get

# List available devices
flutter devices

# Run the app
flutter run
```

## Alternative: Using VS Code

1. **Install VS Code:**
   - Download from https://code.visualstudio.com/

2. **Install Flutter Extension:**
   - Open VS Code
   - Go to Extensions (Ctrl+Shift+X)
   - Search for "Flutter" and install it
   - This will also install the "Dart" extension

3. **Run the app:**
   - Open the `rider_app` folder in VS Code
   - Press F5 or click "Run and Debug"
   - Select a device if prompted
   - The app will launch on the emulator

