# 🚗 Ride Hailing Application

A complete ride-hailing application with real-time location tracking, built with Spring Boot (Backend) and Flutter (Frontend).

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Troubleshooting](#troubleshooting)
- [Documentation](#documentation)

---

## ✨ Features

### Backend
- ✅ User registration (Rider & Driver)
- ✅ JWT-based authentication
- ✅ Password hashing with BCrypt
- ✅ Real-time location tracking via WebSocket
- ✅ Ride management system
- ✅ PostgreSQL database integration
- ✅ RESTful API

### Frontend
- ✅ Beautiful and modern UI
- ✅ User authentication (Login/Register)
- ✅ Interactive map with OpenStreetMap
- ✅ Real-time GPS location tracking
- ✅ WebSocket integration for live updates
- ✅ Persistent login (remembers user)
- ✅ Book ride functionality
- ✅ Auto-detection of location (Bangalore, India)

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.x
- **Language:** Java 17+
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT
- **Build Tool:** Maven
- **WebSocket:** Spring WebSocket

### Frontend
- **Framework:** Flutter
- **Language:** Dart
- **State Management:** Provider
- **Maps:** flutter_map (OpenStreetMap)
- **Location:** geolocator
- **HTTP:** http package
- **Storage:** flutter_secure_storage

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required Software
1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/
   - Verify: `java -version`

2. **Maven 3.6+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **PostgreSQL 12+**
   - Download from: https://www.postgresql.org/download/
   - Default port: 5432 (or 5433 if using custom port)
   - Verify: `psql --version`

4. **Flutter SDK 3.0+**
   - Download from: https://flutter.dev/docs/get-started/install
   - Verify: `flutter --version`

5. **Android Studio** (for Android development)
   - Download from: https://developer.android.com/studio
   - Install Android SDK and emulator

6. **Git** (for version control)
   - Download from: https://git-scm.com/downloads

### Optional
- **pgAdmin** (for database management)
- **Postman** (for API testing)

---

## 📁 Project Structure

```
New_uber/
├── src/                          # Backend (Spring Boot)
│   └── main/
│       ├── java/
│       │   └── com/ridehailing/backend/
│       │       ├── config/       # Configuration classes
│       │       ├── controller/   # REST controllers
│       │       ├── entity/       # JPA entities
│       │       ├── repository/   # Data repositories
│       │       ├── service/      # Business logic
│       │       ├── security/     # Security configuration
│       │       └── websocket/    # WebSocket handlers
│       └── resources/
│           └── application.properties
│
├── rider_app/                    # Frontend (Flutter)
│   ├── lib/
│   │   ├── config/              # API configuration
│   │   ├── models/              # Data models
│   │   ├── providers/           # State management
│   │   ├── screens/             # UI screens
│   │   └── services/            # API services
│   └── android/                  # Android configuration
│
├── database/                     # Database scripts
│   └── migrations/              # SQL migration files
│
└── README.md                     # This file
```

---

## 🚀 Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/mukheeth/rider-app.git
cd rider-app
```

### Step 2: Database Setup

#### 2.1 Install and Start PostgreSQL

1. Install PostgreSQL if not already installed
2. Start PostgreSQL service
3. Note your PostgreSQL:
   - **Port:** (default: 5432, or check your pgAdmin)
   - **Password:** (your PostgreSQL password)
   - **Username:** (usually `postgres`)

#### 2.2 Create Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE ridehailing;

# Exit psql
\q
```

**OR** use pgAdmin:
1. Open pgAdmin
2. Right-click on "Databases" → "Create" → "Database"
3. Name: `ridehailing`
4. Click "Save"

#### 2.3 Run Migrations

```bash
# Navigate to database folder
cd database

# Run migrations (PowerShell)
.\run_migrations.ps1

# OR manually run SQL files in order:
# 1. 001_create_roles_table.sql
# 2. 002_create_users_table.sql
# 3. 003_create_riders_table.sql
# 4. 004_create_drivers_table.sql
# 5. 005_create_vehicles_table.sql
# 6. 006_create_rides_table.sql
```

### Step 3: Backend Configuration

#### 3.1 Update Database Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/ridehailing
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
```

**Important:** Update these values to match your PostgreSQL setup:
- `spring.datasource.url` - Change port if needed (5432 or 5433)
- `spring.datasource.password` - Your PostgreSQL password

#### 3.2 Build Backend

```bash
# From project root
mvn clean install
```

### Step 4: Frontend Configuration

#### 4.1 Install Flutter Dependencies

```bash
# Navigate to Flutter app
cd rider_app

# Get dependencies
flutter pub get
```

#### 4.2 Configure API Endpoint

Edit `rider_app/lib/config/api_config.dart`:

**For Android Emulator:**
```dart
static String get baseUrl => 'http://10.0.2.2:8080';
```

**For Physical Device (USB):**
```dart
static String get baseUrl => 'http://localhost:8080';
```
Then run: `adb reverse tcp:8080 tcp:8080`

**For Physical Device (Network):**
```dart
static String get baseUrl => 'http://192.168.0.125:8080'; // Your computer's IP
```

---

## ▶️ Running the Application

### Backend

#### Option 1: Using Maven

```bash
# From project root
mvn spring-boot:run
```

#### Option 2: Using IDE

1. Open project in IntelliJ IDEA or Eclipse
2. Run `RideHailingApplication.java`
3. Wait for: `Started RideHailingApplication`

**Backend will run on:** `http://localhost:8080`

### Frontend

#### For Android Emulator

1. **Start Android Emulator**
   - Open Android Studio
   - Tools → Device Manager → Start emulator

2. **Run Flutter App**
   ```bash
   cd rider_app
   flutter run
   ```

#### For Physical Device (USB)

1. **Enable USB Debugging** on your Android device
2. **Connect device** via USB
3. **Set up ADB forwarding:**
   ```bash
   adb reverse tcp:8080 tcp:8080
   ```
4. **Run Flutter App:**
   ```bash
   cd rider_app
   flutter devices  # List devices
   flutter run -d <device-id>
   ```

#### For Physical Device (Network)

1. **Find your computer's IP:**
   ```bash
   # Windows
   ipconfig
   
   # Look for IPv4 Address (e.g., 192.168.0.125)
   ```

2. **Update API config** to use your IP
3. **Connect device to same Wi-Fi**
4. **Run Flutter App**

---

## ⚙️ Configuration

### Backend Configuration

**File:** `src/main/resources/application.properties`

| Property | Description | Default |
|----------|-------------|---------|
| `spring.datasource.url` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5433/ridehailing` |
| `spring.datasource.username` | Database username | `postgres` |
| `spring.datasource.password` | Database password | `root` |
| `server.port` | Backend server port | `8080` |
| `spring.jpa.show-sql` | Show SQL queries | `true` |

### Frontend Configuration

**File:** `rider_app/lib/config/api_config.dart`

| Environment | Base URL |
|-------------|----------|
| Emulator | `http://10.0.2.2:8080` |
| USB Device | `http://localhost:8080` (with ADB forwarding) |
| Network | `http://<your-ip>:8080` |

---

## 📡 API Endpoints

### Authentication

```
POST   /api/v1/auth/register/rider    - Register as rider
POST   /api/v1/auth/register/driver   - Register as driver
POST   /api/v1/auth/login              - User login
POST   /api/v1/auth/refresh            - Refresh token
POST   /api/v1/auth/logout             - User logout
```

### Rides

```
POST   /api/v1/rides                   - Create ride
GET    /api/v1/rides/{id}              - Get ride details
PUT    /api/v1/rides/{id}/accept       - Accept ride
PUT    /api/v1/rides/{id}/complete     - Complete ride
```

### WebSocket

```
WS     /ws?token={token}               - Real-time location updates
```

### Health Check

```
GET    /health                         - Health check endpoint
```

**Full API documentation:** See `API_CONTRACTS.md`

---

## 🔧 Troubleshooting

### Backend Issues

#### Database Connection Failed
```
Error: Connection refused
```
**Solution:**
1. Check PostgreSQL is running
2. Verify port in `application.properties` (5432 or 5433)
3. Check password is correct
4. Test connection: `psql -U postgres -h localhost -p 5433`

#### Port Already in Use
```
Error: Port 8080 is already in use
```
**Solution:**
1. Change port in `application.properties`: `server.port=8081`
2. Or stop the process using port 8080

### Frontend Issues

#### Map Tiles Not Loading
**Solution:**
1. Check internet connection on device/emulator
2. Enable Wi-Fi or mobile data
3. Check DNS settings (for emulator)

#### Login Not Working on Physical Device
**Solution:**
1. Set up ADB forwarding: `adb reverse tcp:8080 tcp:8080`
2. Or use network IP in API config
3. Make sure backend is running

#### Location Not Updating
**Solution:**
1. Grant location permissions
2. Enable GPS on device
3. Check location services are enabled

### Common Commands

```bash
# Check database connection
psql -U postgres -d ridehailing

# Check backend is running
curl http://localhost:8080/health

# Check ADB devices
adb devices

# Check ADB port forwarding
adb reverse --list

# Flutter clean and rebuild
cd rider_app
flutter clean
flutter pub get
flutter run
```

**More troubleshooting:** See individual fix guides in project root

---

## 📚 Documentation

### Main Documentation Files

- **`COMPLETE_APPLICATION_GUIDE.md`** - Complete architecture and flow guide
- **`PROJECT_OVERVIEW.md`** - Project overview and features
- **`API_CONTRACTS.md`** - API documentation
- **`DATABASE_SCHEMA.md`** - Database schema details

### Setup Guides

- **`database/DATABASE_CONNECTION_GUIDE.md`** - Database setup
- **`rider_app/SETUP_INSTRUCTIONS.md`** - Flutter setup
- **`START_BACKEND.md`** - Backend startup guide

### Troubleshooting Guides

- **`FIX_PHYSICAL_DEVICE_LOGIN.md`** - Fix login on physical device
- **`FIX_LOCATION_BANGALORE.md`** - Fix location issues
- **`FIX_MAP_TILES_PHYSICAL_DEVICE.md`** - Fix map loading

---

## 🧪 Testing

### Test Credentials

See `database/TEST_CREDENTIALS.md` for test accounts.

### Quick Test

1. **Register a new user:**
   - Email: `test@example.com`
   - Password: `password123`
   - Role: Rider

2. **Login with credentials**

3. **Check location tracking:**
   - Should show Bangalore on map
   - Location updates every 10 seconds

---

## 📱 Screenshots

### Features
- User Registration (Rider/Driver)
- Login Screen
- Home Screen with Map
- Real-time Location Tracking
- Book Ride Screen

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Mukheeth**
- GitHub: [@mukheeth](https://github.com/mukheeth)

---

## 🙏 Acknowledgments

- OpenStreetMap for map tiles
- Spring Boot community
- Flutter community
- All contributors

---

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review the documentation files
3. Open an issue on GitHub

---

## 🎯 Quick Start Summary

```bash
# 1. Setup Database
psql -U postgres
CREATE DATABASE ridehailing;
\q
cd database
.\run_migrations.ps1

# 2. Configure Backend
# Edit src/main/resources/application.properties
# Update database password and port

# 3. Start Backend
mvn spring-boot:run

# 4. Configure Frontend
# Edit rider_app/lib/config/api_config.dart
# Set correct baseUrl

# 5. Run Frontend
cd rider_app
flutter pub get
flutter run
```

**That's it! Your ride-hailing app is running! 🚀**

---

## ✅ Checklist

Before running, ensure:
- [ ] PostgreSQL is installed and running
- [ ] Database `ridehailing` is created
- [ ] Migrations are run
- [ ] Backend `application.properties` is configured
- [ ] Frontend `api_config.dart` is configured
- [ ] Java 17+ is installed
- [ ] Flutter SDK is installed
- [ ] Android SDK is installed (for Android)

---

**Happy Coding! 🎉**

