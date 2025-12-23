# Android Setup Guide

## Current Status
- ✅ Android SDK found at: `C:\Users\mukee\AppData\Local\Android\Sdk`
- ❌ Command-line tools missing
- ❌ Android licenses not accepted

## Quick Fix Steps

### Step 1: Set ANDROID_HOME Environment Variable

1. Press `Win + X` and select **System** (or right-click **This PC** → **Properties**)
2. Click **Advanced system settings**
3. Click **Environment Variables**
4. Under **User variables**, click **New**
5. Variable name: `ANDROID_HOME`
6. Variable value: `C:\Users\mukee\AppData\Local\Android\Sdk`
7. Click **OK**

### Step 2: Add to PATH

1. In the same **Environment Variables** window, find **Path** under **User variables**
2. Click **Edit**
3. Click **New** and add: `%ANDROID_HOME%\platform-tools`
4. Click **New** and add: `%ANDROID_HOME%\tools`
5. Click **New** and add: `%ANDROID_HOME%\cmdline-tools\latest\bin`
6. Click **OK** on all windows

### Step 3: Install Command-Line Tools

**Option A: Using Android Studio (Easiest)**
1. Download Android Studio from: https://developer.android.com/studio
2. Install it (it will detect your existing SDK)
3. Open Android Studio → **More Actions** → **SDK Manager**
4. Go to **SDK Tools** tab
5. Check **Android SDK Command-line Tools (latest)**
6. Click **Apply** and install

**Option B: Manual Installation (No Android Studio)**
1. Download command-line tools from: https://developer.android.com/studio#command-line-tools-only
2. Extract the zip file
3. Create folder: `C:\Users\mukee\AppData\Local\Android\Sdk\cmdline-tools`
4. Extract the contents into: `C:\Users\mukee\AppData\Local\Android\Sdk\cmdline-tools\latest`
   (The folder structure should be: `cmdline-tools\latest\bin\sdkmanager.bat`)

### Step 4: Restart Terminal

**IMPORTANT:** Close and reopen your terminal/PowerShell after setting environment variables.

### Step 5: Accept Android Licenses

After restarting terminal, run:
```powershell
flutter doctor --android-licenses
```

Press `y` to accept all licenses.

### Step 6: Verify Setup

Run:
```powershell
flutter doctor
```

All Android checks should now pass! ✅

## Alternative: Quick PowerShell Script

If you prefer, I can create a PowerShell script to automate steps 1-2 (setting environment variables).


