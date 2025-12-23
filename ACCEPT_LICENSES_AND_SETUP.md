# Accept Android Licenses & Setup Emulator

## Step 1: Accept Android Licenses

Run this command in PowerShell and type `y` for each prompt:

```powershell
flutter doctor --android-licenses
```

**You will see prompts like:**
```
Review licenses that have not been accepted (y/N)? y
[License text...]
Accept? (y/N): y
```

Type `y` and press Enter for each license (typically 5-7 licenses).

After completing, verify:
```powershell
flutter doctor
```

The Android toolchain should now show ✅ instead of ❌

---

## Step 2: Set Up Android Emulator

### Option A: Using Android Studio (Recommended)

1. **Open Android Studio**
2. Click **More Actions** → **Virtual Device Manager** (or **Tools** → **Device Manager**)
3. Click **Create Device** (or **+ Create Virtual Device**)
4. Select a device:
   - Choose **Phone** category
   - Select **Pixel 5** or **Pixel 6** (or any phone you prefer)
   - Click **Next**
5. Select a system image:
   - Choose **Release Name** (e.g., **Tiramisu**, **UpsideDownCake**)
   - Or any API level **30+** (recommended: **API 33** or **API 34**)
   - If you see **Download** next to it, click to download first
   - Click **Next**
6. Verify configuration and click **Finish**
7. Click the **Play** button (▶️) next to your emulator to start it

### Option B: Using Command Line

1. List available emulators:
   ```powershell
   flutter emulators
   ```

2. If you see any listed, launch one:
   ```powershell
   flutter emulators --launch <emulator_id>
   ```

3. If no emulators are listed, create one via Android Studio first (Option A)

---

## Step 3: Verify Setup

1. **Check if emulator is running:**
   ```powershell
   flutter devices
   ```
   You should see an Android device listed.

2. **Verify everything:**
   ```powershell
   flutter doctor
   ```
   All Android-related items should show ✅

---

## Step 4: Run Your Flutter App

Once the emulator is running:

```powershell
cd rider_app
flutter pub get
flutter run
```

The app will install and launch on the emulator! 🎉

---

## Troubleshooting

**If licenses command hangs:**
- Try running: `sdkmanager --licenses` instead
- Or accept licenses through Android Studio: **Tools** → **SDK Manager** → **SDK Tools** → Check licenses

**If no emulators available:**
- Make sure you installed an Android system image in Android Studio SDK Manager
- Create at least one virtual device in Android Studio Device Manager

**If emulator is slow:**
- Enable Hardware Acceleration in BIOS (VT-x/AMD-V)
- Increase emulator RAM in AVD settings (Settings → Advanced)


