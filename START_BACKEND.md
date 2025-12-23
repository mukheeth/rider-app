# How to Start the Backend

## Quick Start

### Option 1: Using IDE (Easiest - Recommended)

**IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. File → Open → Select `C:\New_uber` folder
3. Wait for Maven to sync dependencies (automatic)
4. Find `RideHailingApplication.java` in `src/main/java/com/ridehailing/backend/`
5. Right-click on the file → **Run 'RideHailingApplication.main()'**
6. Check the console for: `Started RideHailingApplication`

**VS Code:**
1. Install "Extension Pack for Java" extension
2. Open `C:\New_uber` folder in VS Code
3. Open `src/main/java/com/ridehailing/backend/RideHailingApplication.java`
4. Click the "Run" button above the `main` method
5. Or press `F5` to debug/run

**Eclipse:**
1. File → Import → Existing Maven Projects
2. Select `C:\New_uber` folder
3. Right-click project → Run As → Spring Boot App

### Option 2: Install Maven and Use Command Line

**Install Maven:**
1. Download from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven` (or any folder)
3. Add to PATH:
   - Add `C:\Program Files\Apache\maven\bin` to System Environment Variables → PATH
   - Restart terminal

**Then run:**
```powershell
cd C:\New_uber
mvn spring-boot:run
```

### Option 3: Use Maven Wrapper (If Available)

If you have `mvnw` or `mvnw.cmd` in the project root:
```powershell
cd C:\New_uber
.\mvnw spring-boot:run
```

## Prerequisites

### PostgreSQL Database

The backend requires PostgreSQL. Make sure:

1. **PostgreSQL is installed and running**
   - Default config expects: `localhost:5432`
   - Database name: `ridehailing`
   - Username: `postgres`
   - Password: `postgres`

2. **Create the database** (if not exists):
   ```sql
   CREATE DATABASE ridehailing;
   ```

3. **Check if PostgreSQL is running:**
   ```powershell
   # Check if port 5432 is in use
   netstat -an | findstr :5432
   ```

### Java 21

Make sure Java 21 is installed:
```powershell
java -version
```

Should show: `openjdk version "21"` or similar

### Maven

Check if Maven is installed:
```powershell
mvn -version
```

## Troubleshooting

### "Connection refused" to PostgreSQL
- Make sure PostgreSQL service is running
- Check credentials in `src/main/resources/application.properties`
- Verify database `ridehailing` exists

### Port 8080 already in use
- Another application is using port 8080
- Stop the other application or change port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Java version mismatch
- Backend requires Java 21
- Install Java 21 or update `JAVA_HOME`

## Verify Backend is Running

Once started, test in browser:
- Health: `http://localhost:8080/health` → Should return `{"status":"UP"}`

Then your Flutter app should connect successfully!

