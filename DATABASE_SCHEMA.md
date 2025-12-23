# Ride-Hailing Platform - PostgreSQL Schema Design

## Schema Overview

Normalized to 3NF with proper relationships, constraints, and indexes.

---

## Tables

### users
**Description:** Base user table storing common user information

**Columns:**
- `user_id` - UUID (Primary Key)
- `email` - VARCHAR(255) (Unique, Not Null)
- `password_hash` - VARCHAR(255) (Not Null)
- `phone_number` - VARCHAR(20) (Not Null)
- `first_name` - VARCHAR(100) (Not Null)
- `last_name` - VARCHAR(100) (Not Null)
- `role_id` - INTEGER (Foreign Key to roles.role_id, Not Null)
- `status` - VARCHAR(20) (Not Null, Default: 'ACTIVE') - Values: 'ACTIVE', 'INACTIVE', 'SUSPENDED'
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `user_id`
- Unique Index: `email`
- Index: `phone_number`
- Index: `role_id`
- Index: `status`

---

### roles
**Description:** User roles (RIDER, DRIVER, ADMIN)

**Columns:**
- `role_id` - SERIAL (Primary Key)
- `role_name` - VARCHAR(50) (Unique, Not Null) - Values: 'RIDER', 'DRIVER', 'ADMIN'
- `description` - VARCHAR(255)

**Indexes:**
- Primary Key: `role_id`
- Unique Index: `role_name`

---

### riders
**Description:** Rider-specific information

**Columns:**
- `rider_id` - UUID (Primary Key, Foreign Key to users.user_id)
- `rating` - DECIMAL(3,2) (Default: NULL) - Average rating from 0.00 to 5.00
- `total_rides` - INTEGER (Default: 0, Not Null)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `rider_id`
- Index: `rating`

---

### drivers
**Description:** Driver-specific information

**Columns:**
- `driver_id` - UUID (Primary Key, Foreign Key to users.user_id)
- `license_number` - VARCHAR(50) (Unique, Not Null)
- `license_expiry` - DATE (Not Null)
- `rating` - DECIMAL(3,2) (Default: NULL) - Average rating from 0.00 to 5.00
- `total_rides` - INTEGER (Default: 0, Not Null)
- `total_earnings` - DECIMAL(10,2) (Default: 0.00, Not Null)
- `status` - VARCHAR(20) (Not Null, Default: 'OFFLINE') - Values: 'AVAILABLE', 'BUSY', 'OFFLINE'
- `is_verified` - BOOLEAN (Default: FALSE, Not Null)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `driver_id`
- Unique Index: `license_number`
- Index: `status`
- Index: `is_verified`
- Index: `rating`

---

### vehicles
**Description:** Driver vehicles

**Columns:**
- `vehicle_id` - UUID (Primary Key)
- `driver_id` - UUID (Foreign Key to drivers.driver_id, Not Null)
- `vehicle_model` - VARCHAR(100) (Not Null)
- `vehicle_make` - VARCHAR(100) (Not Null)
- `vehicle_year` - INTEGER (Not Null)
- `vehicle_color` - VARCHAR(50)
- `license_plate` - VARCHAR(20) (Not Null)
- `is_active` - BOOLEAN (Default: TRUE, Not Null)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `vehicle_id`
- Index: `driver_id`
- Unique Index: `license_plate`
- Index: `is_active`

---

### rides
**Description:** Ride records

**Columns:**
- `ride_id` - UUID (Primary Key)
- `rider_id` - UUID (Foreign Key to riders.rider_id, Not Null)
- `driver_id` - UUID (Foreign Key to drivers.driver_id, Nullable)
- `vehicle_id` - UUID (Foreign Key to vehicles.vehicle_id, Nullable)
- `pickup_latitude` - DECIMAL(10,8) (Not Null)
- `pickup_longitude` - DECIMAL(11,8) (Not Null)
- `pickup_address` - VARCHAR(500) (Not Null)
- `dropoff_latitude` - DECIMAL(10,8) (Nullable)
- `dropoff_longitude` - DECIMAL(11,8) (Nullable)
- `dropoff_address` - VARCHAR(500) (Nullable)
- `status` - VARCHAR(20) (Not Null, Default: 'PENDING') - Values: 'PENDING', 'ACCEPTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'
- `estimated_fare` - DECIMAL(10,2) (Nullable)
- `actual_fare` - DECIMAL(10,2) (Nullable)
- `estimated_distance` - DECIMAL(8,2) (Nullable) - In kilometers
- `actual_distance` - DECIMAL(8,2) (Nullable) - In kilometers
- `estimated_duration` - INTEGER (Nullable) - In minutes
- `actual_duration` - INTEGER (Nullable) - In minutes
- `cancelled_by` - VARCHAR(10) (Nullable) - Values: 'RIDER', 'DRIVER'
- `cancellation_reason` - VARCHAR(255) (Nullable)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `accepted_at` - TIMESTAMP (Nullable)
- `started_at` - TIMESTAMP (Nullable)
- `completed_at` - TIMESTAMP (Nullable)
- `cancelled_at` - TIMESTAMP (Nullable)

**Indexes:**
- Primary Key: `ride_id`
- Index: `rider_id`
- Index: `driver_id`
- Index: `vehicle_id`
- Index: `status`
- Index: `created_at`
- Composite Index: `(rider_id, status)`
- Composite Index: `(driver_id, status)`
- Composite Index: `(status, created_at)`

---

### payments
**Description:** Payment transactions for rides

**Columns:**
- `payment_id` - UUID (Primary Key)
- `ride_id` - UUID (Foreign Key to rides.ride_id, Not Null, Unique)
- `rider_id` - UUID (Foreign Key to riders.rider_id, Not Null)
- `driver_id` - UUID (Foreign Key to drivers.driver_id, Not Null)
- `amount` - DECIMAL(10,2) (Not Null)
- `currency` - VARCHAR(3) (Not Null, Default: 'USD')
- `payment_method` - VARCHAR(50) (Not Null) - Values: 'CREDIT_CARD', 'DEBIT_CARD', 'WALLET', etc.
- `payment_status` - VARCHAR(20) (Not Null, Default: 'PENDING') - Values: 'PENDING', 'COMPLETED', 'FAILED', 'REFUNDED'
- `transaction_id` - VARCHAR(255) (Nullable) - External payment gateway transaction ID
- `driver_earnings` - DECIMAL(10,2) (Nullable) - Driver's share after commission
- `platform_commission` - DECIMAL(10,2) (Nullable) - Platform's commission
- `processed_at` - TIMESTAMP (Nullable)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `payment_id`
- Unique Index: `ride_id`
- Index: `rider_id`
- Index: `driver_id`
- Index: `payment_status`
- Index: `transaction_id`
- Index: `created_at`

---

### ride_locations
**Description:** Historical location tracking during active rides (for ETA and tracking)

**Columns:**
- `location_id` - UUID (Primary Key)
- `ride_id` - UUID (Foreign Key to rides.ride_id, Not Null)
- `latitude` - DECIMAL(10,8) (Not Null)
- `longitude` - DECIMAL(11,8) (Not Null)
- `recorded_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `location_id`
- Index: `ride_id`
- Composite Index: `(ride_id, recorded_at)`

---

### driver_locations
**Description:** Current/active driver locations (for matching available drivers)

**Columns:**
- `driver_id` - UUID (Primary Key, Foreign Key to drivers.driver_id)
- `latitude` - DECIMAL(10,8) (Not Null)
- `longitude` - DECIMAL(11,8) (Not Null)
- `updated_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)

**Indexes:**
- Primary Key: `driver_id`
- Index: `updated_at`

---

### refresh_tokens
**Description:** JWT refresh tokens for authentication

**Columns:**
- `token_id` - UUID (Primary Key)
- `user_id` - UUID (Foreign Key to users.user_id, Not Null)
- `token` - VARCHAR(500) (Not Null)
- `expires_at` - TIMESTAMP (Not Null)
- `created_at` - TIMESTAMP (Not Null, Default: CURRENT_TIMESTAMP)
- `revoked_at` - TIMESTAMP (Nullable)

**Indexes:**
- Primary Key: `token_id`
- Index: `user_id`
- Index: `token`
- Index: `expires_at`
- Index: `(token, revoked_at)`

---

## Relationships Summary

### Primary Relationships
- `users.role_id` → `roles.role_id` (Many-to-One)
- `riders.rider_id` → `users.user_id` (One-to-One)
- `drivers.driver_id` → `users.user_id` (One-to-One)
- `vehicles.driver_id` → `drivers.driver_id` (Many-to-One)
- `rides.rider_id` → `riders.rider_id` (Many-to-One)
- `rides.driver_id` → `drivers.driver_id` (Many-to-One)
- `rides.vehicle_id` → `vehicles.vehicle_id` (Many-to-One)
- `payments.ride_id` → `rides.ride_id` (One-to-One)
- `payments.rider_id` → `riders.rider_id` (Many-to-One)
- `payments.driver_id` → `drivers.driver_id` (Many-to-One)
- `ride_locations.ride_id` → `rides.ride_id` (Many-to-One)
- `driver_locations.driver_id` → `drivers.driver_id` (One-to-One)
- `refresh_tokens.user_id` → `users.user_id` (Many-to-One)

---

## Data Types Summary

### UUID
- Used for all primary keys and foreign keys referencing user-related entities
- Provides globally unique identifiers

### VARCHAR
- Variable-length strings with appropriate size limits
- Used for names, addresses, identifiers

### DECIMAL
- Precision numeric types for financial and geographic data
- `DECIMAL(10,2)` for currency amounts
- `DECIMAL(3,2)` for ratings (0.00 to 5.00)
- `DECIMAL(10,8)` and `DECIMAL(11,8)` for latitude/longitude (WGS84 precision)

### INTEGER
- Used for counts, years, durations

### TIMESTAMP
- All date/time fields use TIMESTAMP
- Default to CURRENT_TIMESTAMP where appropriate

### BOOLEAN
- Simple true/false flags

### SERIAL
- Auto-incrementing integer for roles table

---

## Normalization Notes

### 3NF Compliance
- All tables are normalized to 3NF
- No transitive dependencies
- No partial dependencies
- Each non-key attribute depends on the primary key
- Separate tables for users, riders, drivers to avoid NULL values
- Normalized address storage (separate from other attributes)
- Separate payment table to avoid redundancy

### Design Decisions
- Users table as base with role_id for role-based access
- Separate rider and driver tables (one-to-one with users) for role-specific attributes
- Vehicles as separate table (driver can have multiple vehicles over time)
- Ride locations as separate table for historical tracking
- Driver locations as separate table for real-time matching queries
- Payments as separate table to support future payment method expansion
- Refresh tokens in separate table for token management and revocation

---

## Index Strategy

### Primary Key Indexes
- All primary keys automatically indexed by PostgreSQL

### Foreign Key Indexes
- All foreign keys indexed for join performance

### Query Performance Indexes
- Status fields indexed for filtering
- Date/time fields indexed for time-based queries
- Email, phone, license_plate with unique indexes for lookup
- Composite indexes on frequently queried combinations (rider_id + status, driver_id + status, status + created_at)

### Geospatial Considerations
- Latitude/longitude stored as DECIMAL for precision
- For production, consider PostgreSQL PostGIS extension for advanced geospatial queries
- Indexes on location tables support proximity searches

---

## Constraints Summary

### Primary Keys
- All tables have UUID primary keys (except roles with SERIAL)

### Foreign Keys
- All foreign key relationships defined with referential integrity
- Cascade rules should be defined in application layer or with ON DELETE CASCADE where appropriate

### Unique Constraints
- Email addresses must be unique
- License numbers must be unique
- License plates must be unique
- One payment per ride (ride_id unique in payments)

### Check Constraints (Application Level)
- Status values should be validated (enums or check constraints)
- Rating values should be between 0.00 and 5.00
- Date validations (license_expiry > current date, etc.)

### Not Null Constraints
- Critical fields marked as NOT NULL
- Optional fields (dropoff location, cancellation info) are nullable

---

## Table Size Considerations

### Large Tables (High Volume)
- `rides` - Will grow continuously, consider partitioning by date
- `ride_locations` - High frequency updates, consider archiving old records
- `driver_locations` - High frequency updates, TTL-based cleanup recommended

### Medium Tables
- `payments` - Grows with rides
- `users`, `riders`, `drivers` - Moderate growth

### Small Tables
- `roles` - Static reference data
- `vehicles` - Moderate growth

