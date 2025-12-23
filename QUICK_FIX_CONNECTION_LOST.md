# Quick Fix: Lost Connection to Device

## ⚡ Immediate Solution

### Step 1: Restart Everything

```powershell
# 1. Stop Flutter (Ctrl+C if running)
# 2. Restart emulator (close and reopen)
# 3. Run:
cd rider_app
flutter clean
flutter pub get
flutter run
```

---

## 🔍 If That Doesn't Work

### Check ADB Connection

```powershell
adb devices
```

If no device shows:
```powershell
adb kill-server
adb start-server
adb devices
```

### Check Emulator

1. Is emulator still running?
2. Is it frozen? (try clicking on it)
3. Restart emulator if needed

### Check Backend

```powershell
# Is backend running?
# Check: http://localhost:8080/health
```

---

## 🛠️ Code Fixes Applied

I've added safety checks to prevent crashes:
- ✅ Null checks before setState
- ✅ Mounted checks
- ✅ Try-catch in navigation
- ✅ Safe map tap handling

---

## 🚀 Try Again

After restarting:

```powershell
flutter run
```

The app should reconnect! If it still fails, check `TROUBLESHOOTING_CONNECTION_LOST.md` for detailed steps.

---

## 💡 Pro Tip

If this keeps happening:
- Use a physical device instead of emulator
- Or increase emulator RAM in AVD settings
- Or use a lighter emulator (Pixel 4 instead of Pixel 6)

