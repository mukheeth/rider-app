# Driver Registration Implementation Guide

## ✅ What Was Implemented

Driver registration is now fully functional! Here's what was added:

### 1. Database Tables
- **drivers** table - Stores driver-specific information (license, status, earnings)
- **vehicles** table - Stores vehicle information linked to drivers

### 2. Entity Classes
- `Driver.java` - JPA entity for drivers table
- `Vehicle.java` - JPA entity for vehicles table

### 3. Repositories
- `DriverRepository.java` - Database operations for drivers
- `VehicleRepository.java` - Database operations for vehicles

### 4. Service Layer
- `AuthenticationService.registerDriver()` - Complete driver registration logic

### 5. Controller
- `AuthController.registerDriver()` - REST endpoint for driver registration

## 🚀 Setup Steps

### Step 1: Run Database Migrations

**Important:** You need to run the new migrations before testing driver registration!

```powershell
cd database\migrations
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -f 004_create_drivers_table.sql
psql -h localhost -p 5433 -U postgres -d ridehailing -f 005_create_vehicles_table.sql
$env:PGPASSWORD=$null
```

Or run all migrations:
```powershell
cd database\migrations
$env:PGPASSWORD="root"
psql -h localhost -p 5433 -U postgres -d ridehailing -f run_all_migrations.sql
$env:PGPASSWORD=$null
```

### Step 2: Restart Backend

**CRITICAL:** Restart your Spring Boot backend for the changes to take effect:

```powershell
# Stop backend (Ctrl+C)
# Then restart:
mvn clean compile
mvn spring-boot:run
```

### Step 3: Test Driver Registration

Use the Flutter app or test with PowerShell:

```powershell
$body = @{
    email = "driver1@test.com"
    password = "DriverPass123!"
    phoneNumber = "3333333333"
    firstName = "Driver"
    lastName = "One"
    licenseNumber = "DL123456789"
    vehicleModel = "Toyota Camry"
    vehiclePlate = "ABC1234"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/register/driver" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

$response | ConvertTo-Json
```

## 📋 API Contract

### POST /api/v1/auth/register/driver

**Request:**
```json
{
  "email": "string",
  "password": "string",
  "phoneNumber": "string",
  "firstName": "string",
  "lastName": "string",
  "licenseNumber": "string",
  "vehicleModel": "string",
  "vehiclePlate": "string"
}
```

**Success Response (201 Created):**
```json
{
  "userId": "uuid",
  "email": "driver1@test.com",
  "message": "Registration successful. Awaiting verification."
}
```

**Error Responses:**
- `400 Bad Request` - Missing required fields
- `400 Bad Request` - Email/phone/license/plate already registered
- `500 Internal Server Error` - Registration failed

## 🔍 Validation Rules

The service validates:
1. ✅ Email uniqueness
2. ✅ Phone number uniqueness
3. ✅ License number uniqueness
4. ✅ License plate uniqueness
5. ✅ Password hashing (BCrypt)
6. ✅ All required fields present

## 📝 Notes

- **License Expiry:** Automatically set to 1 year from registration date (can be updated later)
- **Vehicle Make/Model:** If `vehicleModel` contains a space, it's split into make and model. Otherwise, make defaults to "Unknown"
- **Vehicle Year:** Defaults to current year (can be updated later)
- **Driver Status:** Defaults to "OFFLINE"
- **Verification:** Driver starts as `is_verified = false` (awaiting admin verification)

## 🧪 Test Credentials

```
Email: driver1@test.com
Password: DriverPass123!
Phone: 3333333333
First Name: Driver
Last Name: One
License Number: DL123456789
Vehicle Model: Toyota Camry
Vehicle Plate: ABC1234
```

## ✅ Verification

After successful registration, verify in database:

```sql
-- Check user
SELECT user_id, email, first_name, last_name, status 
FROM users 
WHERE email = 'driver1@test.com';

-- Check driver
SELECT driver_id, license_number, status, is_verified 
FROM drivers 
WHERE driver_id = (SELECT user_id FROM users WHERE email = 'driver1@test.com');

-- Check vehicle
SELECT vehicle_id, vehicle_make, vehicle_model, license_plate 
FROM vehicles 
WHERE driver_id = (SELECT user_id FROM users WHERE email = 'driver1@test.com');
```

## 🐛 Troubleshooting

### Error: "Driver registration not yet implemented"
- **Solution:** Restart the backend server

### Error: "relation 'drivers' does not exist"
- **Solution:** Run the migration scripts (Step 1 above)

### Error: "DRIVER role not found in database"
- **Solution:** Ensure `001_create_roles_table.sql` was run (it inserts RIDER, DRIVER, ADMIN roles)

### Error: "License number already registered"
- **Solution:** Use a different license number or delete the existing driver record

