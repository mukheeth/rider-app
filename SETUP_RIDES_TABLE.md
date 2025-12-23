# Setup Rides Table - Quick Guide

## ✅ Rides Table Migration Created

I've created the migration file: `database/migrations/006_create_rides_table.sql`

## 🚀 Run the Migration

### Option 1: Run Single Migration

```powershell
cd database\migrations
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -f 006_create_rides_table.sql
$env:PGPASSWORD=$null
```

### Option 2: Run All Migrations

```powershell
cd database\migrations
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -f run_all_migrations.sql
$env:PGPASSWORD=$null
```

## ✅ After Migration

1. **Restart Backend** (if running):
   ```powershell
   # Stop backend (Ctrl+C)
   mvn spring-boot:run
   ```

2. **Test Ride Booking**:
   - Open the app
   - Go to "Book a Ride"
   - Your current location will be automatically set as pickup
   - Select dropoff location on map or enter address
   - Click "Book Ride"

## 📋 What Was Created

- **Rides table** with all required columns
- **Indexes** for performance
- **Foreign keys** to riders, drivers, vehicles
- **Status constraints** (PENDING, ACCEPTED, etc.)

## 🎯 Book Ride Screen Features

✅ **Automatic Current Location** - Gets your location on screen open
✅ **Refresh Location Button** - Update current location
✅ **Map Selection** - Tap map icon to select location visually
✅ **Text Input** - Enter address manually
✅ **Coordinates Display** - Shows lat/long for both locations

The app is now ready to book rides! 🚗

