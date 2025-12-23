# Fix Android SDK Issues

## Current Issues
- ❌ Flutter requires Android SDK 36 (you have 35.0.0)
- ❌ Android BuildTools 28.0.3 is missing
- ❌ Android licenses not accepted

## Solution: Use Android Studio SDK Manager (Easiest)

### Step 1: Open Android Studio SDK Manager

1. Open **Android Studio**
2. Click on **More Actions** (or **Configure** on the welcome screen)
3. Select **SDK Manager**

   OR

   If you have a project open:
   - Go to **File** → **Settings** (or **Android Studio** → **Preferences** on Mac)
   - Navigate to **Appearance & Behavior** → **System Settings** → **Android SDK**

### Step 2: Install Android SDK 36

1. In the **SDK Platforms** tab:
   - Check **Android 14.0 (API 36)** or **Android SDK Platform 36**
   - If you don't see API 36, check **Show Package Details** at the bottom right
   - Look for **Android SDK Platform 36** under any available API level
   
   **Note:** If API 36 is not available yet, you may need to:
   - Check **SDK Update Sites** tab and ensure Google's repository is enabled
   - Or install the latest available SDK (API 35) and update Flutter

2. Click **Apply** or **OK**

### Step 3: Install BuildTools 28.0.3

1. Go to the **SDK Tools** tab
2. Check **Show Package Details** at the bottom right
3. Expand **Android SDK Build-Tools**
4. Check **28.0.3** (you may also see 35.0.0, but we need 28.0.3 specifically)
5. If 28.0.3 is not listed, you can install it via command line (see alternative method below)
6. Click **Apply** or **OK**

### Step 4: Accept Android Licenses

1. Close Android Studio
2. **Restart your terminal/PowerShell** (important!)
3. Run:
   ```powershell
   flutter doctor --android-licenses
   ```
4. Type `y` and press Enter for each license agreement

### Step 5: Verify

Run:
```powershell
flutter doctor
```

All Android issues should now be resolved! ✅

---

## Alternative: Command Line Method

If you prefer using command line, you need to set ANDROID_HOME first:

### Step 1: Set ANDROID_HOME (If Not Already Set)

1. Find your Android SDK path (usually: `C:\Users\mukee\AppData\Local\Android\Sdk`)
2. Set environment variable:
   - Press `Win + X` → **System** → **Advanced system settings** → **Environment Variables**
   - Under **User variables**, click **New**
   - Name: `ANDROID_HOME`
   - Value: `C:\Users\mukee\AppData\Local\Android\Sdk`
   - Click **OK**

3. Add to PATH:
   - Edit **Path** under User variables
   - Add: `%ANDROID_HOME%\cmdline-tools\latest\bin`
   - Add: `%ANDROID_HOME%\platform-tools`
   - Click **OK**

4. **Restart terminal** after setting environment variables

### Step 2: Install SDK 36 and BuildTools

After restarting terminal, run:

```powershell
# Accept licenses first
sdkmanager --licenses

# Install SDK Platform 36
sdkmanager "platforms;android-36"

# Install BuildTools 28.0.3
sdkmanager "build-tools;28.0.3"
```

### Step 3: Verify with Flutter

```powershell
flutter doctor
```

---

## Note: If SDK 36 is Not Available

If Android SDK 36 is not yet available in the SDK Manager:

1. Flutter might be requesting a version that's not released yet
2. Check Flutter version: `flutter --version`
3. Update Flutter to latest: `flutter upgrade`
4. Or install the latest available SDK (usually API 35) and it should work

In most cases, installing SDK 35 (the latest stable) and BuildTools 28.0.3 should be sufficient.


