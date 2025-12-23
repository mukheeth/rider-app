# Rider App

Minimal Flutter Rider app for the ride-hailing platform.

> **Note:** Currently configured for Android development. iOS support will be added later. Flutter is cross-platform, so the same codebase will work for both platforms.

## Quick Start

For detailed setup instructions, see [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)

### Quick Setup Steps

1. **Install Flutter SDK:** (See [FLUTTER_INSTALL.md](FLUTTER_INSTALL.md) for detailed steps)
   - Download from https://docs.flutter.dev/get-started/install/windows
   - Extract to `C:\src\flutter` (or any path without spaces)
   - Add `C:\src\flutter\bin` to PATH (Environment Variables)
   - Restart terminal and run: `flutter --version` to verify

2. **Install Android Studio:**
   - Download from https://developer.android.com/studio
   - Install Android SDK and Emulator
   - Accept licenses: `flutter doctor --android-licenses`

3. **Create and Start Emulator:**
   - Open Android Studio → Virtual Device Manager
   - Create a new device and start it

4. **Run the App:**
   ```powershell
   cd rider_app
   flutter pub get
   flutter run
   ```

### Running the App

Once setup is complete:

1. Get dependencies:
```bash
flutter pub get
```

2. Check available devices:
```bash
flutter devices
```

3. Run on emulator:
```bash
flutter run
```

Or run on a specific device:
```bash
flutter run -d <device-id>
```

### Project Structure

```
lib/
  main.dart          # App entry point
  screens/
    home_screen.dart # Home screen widget
```

