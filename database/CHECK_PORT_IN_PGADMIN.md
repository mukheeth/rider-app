# How to Check Database Port in pgAdmin

## Method 1: Server Properties (Easiest)

1. **Right-click** on your PostgreSQL server in the left sidebar (e.g., "PostgreSQL 18")
2. Select **"Properties"** from the context menu
3. Click on the **"Connection"** tab
4. Look for the **"Port"** field
   - Default PostgreSQL port is **5432**
   - Your configured port will be displayed here

## Method 2: View Server Details

1. **Click** on your PostgreSQL server in the left sidebar
2. Look at the **"Properties"** panel on the right side
3. The port information is usually displayed in the connection details

## Method 3: Using Query Tool

1. **Right-click** on your PostgreSQL server
2. Select **"Query Tool"**
3. Run this SQL query:
   ```sql
   SHOW port;
   ```
   
   Or:
   ```sql
   SELECT setting FROM pg_settings WHERE name = 'port';
   ```

## Method 4: Check Connection String

1. **Right-click** on your PostgreSQL server
2. Select **"Properties"**
3. Go to **"Connection"** tab
4. You'll see the full connection details including:
   - Host name/address
   - Port number
   - Maintenance database
   - Username

## Quick Verification

Once you find the port, verify it matches your application configuration:

**In `src/main/resources/application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ridehailing
                                                      ^^^^
                                                      This is the port
```

If the port in pgAdmin is different from 5432, update your `application.properties` file accordingly.

## Common Ports

- **5432** - Default PostgreSQL port
- **5433** - Alternative port (if 5432 is in use)
- **5434** - Another common alternative

## Troubleshooting

If you can't find the port:
1. Check if the server is connected (green icon vs gray icon)
2. Try refreshing the server (right-click → Refresh)
3. Check the server's connection settings in pgAdmin's server registration

