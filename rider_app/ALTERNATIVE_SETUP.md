# Alternative Setup Methods (Without Android Studio)

Android Studio is **not mandatory**. You have several alternatives:

## Option 1: VS Code + Command Line Tools (Recommended Alternative)

### What You Need:
- Flutter SDK (required)
- VS Code with Flutter extension (optional, any editor works)
- Android SDK Command Line Tools (without Android Studio)
- Emulator or Physical Device

### Setup Steps:

1. **Install Flutter SDK** (same as before)
   - Download from https://docs.flutter.dev/get-started/install/windows
   - Extract and add to PATH

2. **Install Android SDK Command Line Tools** (instead of full Android Studio):
   - Go to https://developer.android.com/studio#command-tools
   - Download "Command line tools only" for Windows
   - Extract to a folder like `C:\Android\cmdline-tools`
   - Create folder structure: `C:\Android\cmdline-tools\latest` and move tools there
   - Add to PATH: `C:\Android\cmdline-tools\latest\bin`
   - Set ANDROID_HOME environment variable: `C:\Android`

3. **Install Android SDK via Command Line:**
   ```powershell
   # Install SDK platform and build tools
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" "emulator"
   
   # Accept licenses
   sdkmanager --licenses
   ```

4. **Create AVD via Command Line:**
   ```powershell
   # List available system images
   sdkmanager --list | findstr "system-images"
   
   # Install a system image (example for API 34)
   sdkmanager "system-images;android-34;google_apis;x86_64"
   
   # Create AVD
   avdmanager create avd -n pixel_6 -k "system-images;android-34;google_apis;x86_64" -d "pixel_6"
   
   # List AVDs
   avdmanager list avd
   
   # Start emulator
   emulator -avd pixel_6
   ```

5. **Install VS Code (Optional):**
   - Download VS Code: https://code.visualstudio.com/
   - Install "Flutter" extension
   - Open `rider_app` folder in VS Code
   - Press F5 to run

## Option 2: Use a Physical Android Device

If you have an Android phone, you can skip the emulator entirely:

1. **Enable Developer Options on your phone:**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → Developer Options
   - Enable "USB Debugging"

2. **Connect via USB:**
   - Connect phone to computer via USB
   - On phone, allow USB debugging when prompted
   - Verify device is detected:
     ```powershell
     flutter devices
     ```

3. **Run the app:**
   ```powershell
   cd rider_app
   flutter pub get
   flutter run
   ```

## Option 3: Use Genymotion or Other Emulators

You can use third-party emulators:

1. **Genymotion:**
   - Download from https://www.genymotion.com/
   - Create a virtual device
   - Start it
   - Flutter will detect it: `flutter devices`
   - Run: `flutter run`

2. **Bluestacks** (can work but not officially supported)

## Option 4: Use Chrome (Web Development)

Flutter can also run on web (though for mobile app testing, this is limited):

1. **Enable web support:**
   ```powershell
   flutter config --enable-web
   ```

2. **Run on Chrome:**
   ```powershell
   flutter run -d chrome
   ```

## Comparison

| Method | Pros | Cons |
|--------|------|------|
| **Android Studio** | Easiest, all-in-one, GUI for emulator management | Large download (~1GB), includes IDE you might not use |
| **Command Line Tools** | Smaller download, more control | More manual setup, command-line only |
| **Physical Device** | Real device testing, no emulator needed | Requires Android phone, USB connection |
| **Genymotion** | Fast emulator, good performance | Requires account, paid for commercial use |
| **Chrome (Web)** | No Android setup needed | Limited mobile features, not true mobile testing |

## Recommended Approach

**For beginners:** Use Android Studio (easiest setup)

**For experienced developers:** Use VS Code + Command Line Tools or Physical Device

**For quick testing:** Use Physical Device (if available)

## Minimum Requirements

At minimum, you need:
1. ✅ **Flutter SDK** (mandatory)
2. ✅ **Android SDK** (can be installed via command line tools)
3. ✅ **Emulator OR Physical Device** (mandatory for Android testing)

Android Studio just bundles all of these together for convenience!

