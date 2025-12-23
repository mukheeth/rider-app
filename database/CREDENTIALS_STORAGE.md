# Credentials Storage Implementation

## ✅ Implementation Complete

User registration credentials are now stored in the PostgreSQL database with proper password hashing.

## Database Tables

### 1. `roles` Table
Stores user roles (RIDER, DRIVER, ADMIN)
- **Location**: `database/migrations/001_create_roles_table.sql`
- **Pre-populated**: Yes, with default roles

### 2. `users` Table
Stores user credentials and information:
- **Location**: `database/migrations/002_create_users_table.sql`
- **Columns**:
  - `user_id` (UUID, Primary Key)
  - `email` (VARCHAR(255), Unique, Not Null)
  - `password_hash` (VARCHAR(255), Not Null) - **BCrypt hashed password**
  - `phone_number` (VARCHAR(20), Not Null)
  - `first_name` (VARCHAR(100), Not Null)
  - `last_name` (VARCHAR(100), Not Null)
  - `role_id` (INTEGER, Foreign Key to roles)
  - `status` (VARCHAR(20), Default: 'ACTIVE')
  - `created_at`, `updated_at` (TIMESTAMP)

### 3. `riders` Table
Stores rider-specific information:
- **Location**: `database/migrations/003_create_riders_table.sql`
- **Columns**:
  - `rider_id` (UUID, Primary Key, Foreign Key to users.user_id)
  - `rating` (DECIMAL)
  - `total_rides` (INTEGER, Default: 0)
  - `created_at`, `updated_at` (TIMESTAMP)

## Password Security

- **Hashing Algorithm**: BCrypt
- **Strength Factor**: 12 (configurable in `PasswordEncoderConfig.java`)
- **Storage**: Only hashed passwords are stored, never plain text

## Code Structure

### Entity Classes
- `RoleEntity.java` - Maps to `roles` table
- `User.java` - Maps to `users` table
- `Rider.java` - Maps to `riders` table

### Repositories
- `RoleRepository.java` - CRUD operations for roles
- `UserRepository.java` - CRUD operations for users
- `RiderRepository.java` - CRUD operations for riders

### Services
- `AuthenticationService.java` - Handles user registration and authentication
  - `registerRider()` - Creates user and rider records
  - `authenticate()` - Validates credentials

### Controllers
- `AuthController.java` - REST endpoints
  - `POST /api/v1/auth/register/rider` - Saves user to database
  - `POST /api/v1/auth/login` - Authenticates against database

## Registration Flow

1. User submits registration form with:
   - Email
   - Password (plain text)
   - Phone number
   - First name
   - Last name

2. `AuthController.registerRider()` receives request

3. `AuthenticationService.registerRider()`:
   - Validates email/phone uniqueness
   - Hashes password with BCrypt
   - Creates `User` record in `users` table
   - Creates `Rider` record in `riders` table
   - Returns saved user

4. Response sent to client with user ID

## Login Flow

1. User submits email and password

2. `AuthController.login()` receives request

3. `AuthenticationService.authenticate()`:
   - Finds user by email
   - Verifies password hash matches
   - Checks account status
   - Returns authenticated user

4. JWT tokens generated and returned

## Verification

### Check if tables exist:
```sql
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;
```

### Check registered users:
```sql
SELECT user_id, email, first_name, last_name, status, created_at 
FROM users;
```

### Check roles:
```sql
SELECT * FROM roles;
```

### Check riders:
```sql
SELECT r.rider_id, u.email, u.first_name, u.last_name, r.total_rides 
FROM riders r 
JOIN users u ON r.rider_id = u.user_id;
```

## Security Features

✅ **Password Hashing**: BCrypt with strength factor 12
✅ **Email Uniqueness**: Enforced at database level
✅ **Phone Number Uniqueness**: Enforced at application level
✅ **Account Status**: ACTIVE/INACTIVE/SUSPENDED
✅ **Password Never Stored in Plain Text**: Only hashes stored

## Next Steps

To test the implementation:

1. **Restart the backend**:
   ```powershell
   mvn spring-boot:run
   ```

2. **Register a new user** via the Flutter app

3. **Verify in database**:
   ```sql
   SELECT user_id, email, first_name, last_name, status FROM users;
   ```

4. **Login with the registered credentials**

## Files Created/Modified

### Database Migrations
- `database/migrations/001_create_roles_table.sql`
- `database/migrations/002_create_users_table.sql`
- `database/migrations/003_create_riders_table.sql`
- `database/migrations/run_all_migrations.sql`
- `database/run_migrations.ps1`

### Java Classes
- `src/main/java/com/ridehailing/backend/entity/RoleEntity.java`
- `src/main/java/com/ridehailing/backend/entity/User.java`
- `src/main/java/com/ridehailing/backend/entity/Rider.java`
- `src/main/java/com/ridehailing/backend/repository/RoleRepository.java`
- `src/main/java/com/ridehailing/backend/repository/UserRepository.java`
- `src/main/java/com/ridehailing/backend/repository/RiderRepository.java`
- `src/main/java/com/ridehailing/backend/config/PasswordEncoderConfig.java`
- `src/main/java/com/ridehailing/backend/service/AuthenticationService.java`
- `src/main/java/com/ridehailing/backend/controller/AuthController.java` (updated)

