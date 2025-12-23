# Ride-Hailing Platform - API Contracts

## Base URL
- All REST APIs: `/api/v1`
- WebSocket: `wss://domain.com/ws`

## Authentication
- All protected endpoints require JWT token in Authorization header: `Authorization: Bearer <token>`

---

## Authentication APIs

### POST /api/v1/auth/register/rider
**Description:** Register a new rider account

**Request:**
```json
{
  "email": "string",
  "password": "string",
  "phoneNumber": "string",
  "firstName": "string",
  "lastName": "string"
}
```

**Response:** 201 Created
```json
{
  "userId": "string",
  "email": "string",
  "message": "Registration successful"
}
```

---

### POST /api/v1/auth/register/driver
**Description:** Register a new driver account

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

**Response:** 201 Created
```json
{
  "userId": "string",
  "email": "string",
  "message": "Registration successful. Awaiting verification."
}
```

---

### POST /api/v1/auth/login
**Description:** Login and receive JWT tokens

**Request:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response:** 200 OK
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "userId": "string",
    "email": "string",
    "role": "RIDER|DRIVER|ADMIN"
  }
}
```

---

### POST /api/v1/auth/refresh
**Description:** Refresh access token using refresh token

**Request:**
```json
{
  "refreshToken": "string"
}
```

**Response:** 200 OK
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

### POST /api/v1/auth/logout
**Description:** Logout and invalidate tokens

**Request:**
```json
{
  "refreshToken": "string"
}
```

**Response:** 200 OK
```json
{
  "message": "Logout successful"
}
```

---

## Rider APIs

### GET /api/v1/rider/profile
**Description:** Get rider profile

**Response:** 200 OK
```json
{
  "userId": "string",
  "email": "string",
  "phoneNumber": "string",
  "firstName": "string",
  "lastName": "string",
  "rating": 4.5
}
```

---

### PUT /api/v1/rider/profile
**Description:** Update rider profile

**Request:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string"
}
```

**Response:** 200 OK
```json
{
  "message": "Profile updated successfully"
}
```

---

### GET /api/v1/rider/rides
**Description:** Get rider ride history

**Query Parameters:**
- `page`: number (default: 0)
- `size`: number (default: 20)

**Response:** 200 OK
```json
{
  "rides": [
    {
      "rideId": "string",
      "driverId": "string",
      "driverName": "string",
      "pickupLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "dropoffLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "status": "COMPLETED|CANCELLED",
      "fare": 0.0,
      "createdAt": "2024-01-01T00:00:00Z",
      "completedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 0,
  "totalPages": 0
}
```

---

## Driver APIs

### GET /api/v1/driver/profile
**Description:** Get driver profile

**Response:** 200 OK
```json
{
  "userId": "string",
  "email": "string",
  "phoneNumber": "string",
  "firstName": "string",
  "lastName": "string",
  "licenseNumber": "string",
  "vehicleModel": "string",
  "vehiclePlate": "string",
  "rating": 4.5,
  "status": "AVAILABLE|BUSY|OFFLINE"
}
```

---

### PUT /api/v1/driver/profile
**Description:** Update driver profile

**Request:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "vehicleModel": "string",
  "vehiclePlate": "string"
}
```

**Response:** 200 OK
```json
{
  "message": "Profile updated successfully"
}
```

---

### PUT /api/v1/driver/status
**Description:** Update driver availability status

**Request:**
```json
{
  "status": "AVAILABLE|BUSY|OFFLINE"
}
```

**Response:** 200 OK
```json
{
  "message": "Status updated successfully"
}
```

---

### PUT /api/v1/driver/location
**Description:** Update driver current location

**Request:**
```json
{
  "latitude": 0.0,
  "longitude": 0.0
}
```

**Response:** 200 OK
```json
{
  "message": "Location updated successfully"
}
```

---

### GET /api/v1/driver/rides
**Description:** Get driver ride history

**Query Parameters:**
- `page`: number (default: 0)
- `size`: number (default: 20)

**Response:** 200 OK
```json
{
  "rides": [
    {
      "rideId": "string",
      "riderId": "string",
      "riderName": "string",
      "pickupLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "dropoffLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "status": "COMPLETED|CANCELLED",
      "fare": 0.0,
      "earnings": 0.0,
      "createdAt": "2024-01-01T00:00:00Z",
      "completedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 0,
  "totalPages": 0
}
```

---

### GET /api/v1/driver/earnings
**Description:** Get driver earnings summary

**Query Parameters:**
- `startDate`: string (ISO date)
- `endDate`: string (ISO date)

**Response:** 200 OK
```json
{
  "totalEarnings": 0.0,
  "totalRides": 0,
  "period": {
    "startDate": "2024-01-01T00:00:00Z",
    "endDate": "2024-01-31T23:59:59Z"
  }
}
```

---

## Ride Lifecycle APIs

### POST /api/v1/rides
**Description:** Create a ride request (Rider only)

**Request:**
```json
{
  "pickupLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "dropoffLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  }
}
```

**Response:** 201 Created
```json
{
  "rideId": "string",
  "status": "PENDING",
  "estimatedFare": 0.0,
  "estimatedArrivalTime": 5
}
```

---

### GET /api/v1/rides/{rideId}
**Description:** Get ride details

**Response:** 200 OK
```json
{
  "rideId": "string",
  "riderId": "string",
  "driverId": "string",
  "pickupLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "dropoffLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "status": "PENDING|ACCEPTED|IN_PROGRESS|COMPLETED|CANCELLED",
  "fare": 0.0,
  "driver": {
    "driverId": "string",
    "name": "string",
    "rating": 4.5,
    "vehicleModel": "string",
    "vehiclePlate": "string"
  },
  "createdAt": "2024-01-01T00:00:00Z",
  "acceptedAt": "2024-01-01T00:00:00Z",
  "startedAt": "2024-01-01T00:00:00Z",
  "completedAt": "2024-01-01T00:00:00Z"
}
```

---

### POST /api/v1/rides/{rideId}/accept
**Description:** Driver accepts a ride request

**Response:** 200 OK
```json
{
  "rideId": "string",
  "status": "ACCEPTED",
  "message": "Ride accepted successfully"
}
```

---

### POST /api/v1/rides/{rideId}/reject
**Description:** Driver rejects a ride request

**Response:** 200 OK
```json
{
  "rideId": "string",
  "status": "REJECTED",
  "message": "Ride rejected"
}
```

---

### POST /api/v1/rides/{rideId}/start
**Description:** Driver starts the ride (arrived at pickup)

**Response:** 200 OK
```json
{
  "rideId": "string",
  "status": "IN_PROGRESS",
  "message": "Ride started"
}
```

---

### POST /api/v1/rides/{rideId}/complete
**Description:** Driver completes the ride

**Request:**
```json
{
  "dropoffLocation": {
    "latitude": 0.0,
    "longitude": 0.0
  }
}
```

**Response:** 200 OK
```json
{
  "rideId": "string",
  "status": "COMPLETED",
  "fare": 0.0,
  "message": "Ride completed successfully"
}
```

---

### POST /api/v1/rides/{rideId}/cancel
**Description:** Cancel a ride (Rider or Driver)

**Request:**
```json
{
  "reason": "string"
}
```

**Response:** 200 OK
```json
{
  "rideId": "string",
  "status": "CANCELLED",
  "message": "Ride cancelled"
}
```

---

### GET /api/v1/rides/{rideId}/location
**Description:** Get current location of active ride (driver location during ride)

**Response:** 200 OK
```json
{
  "rideId": "string",
  "driverLocation": {
    "latitude": 0.0,
    "longitude": 0.0
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

## Admin APIs

### GET /api/v1/admin/users
**Description:** Get all users (riders and drivers)

**Query Parameters:**
- `role`: string (RIDER|DRIVER)
- `page`: number (default: 0)
- `size`: number (default: 20)

**Response:** 200 OK
```json
{
  "users": [
    {
      "userId": "string",
      "email": "string",
      "firstName": "string",
      "lastName": "string",
      "role": "RIDER|DRIVER",
      "status": "ACTIVE|INACTIVE|SUSPENDED",
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 0,
  "totalPages": 0
}
```

---

### GET /api/v1/admin/users/{userId}
**Description:** Get user details by ID

**Response:** 200 OK
```json
{
  "userId": "string",
  "email": "string",
  "phoneNumber": "string",
  "firstName": "string",
  "lastName": "string",
  "role": "RIDER|DRIVER",
  "status": "ACTIVE|INACTIVE|SUSPENDED",
  "rating": 4.5,
  "createdAt": "2024-01-01T00:00:00Z"
}
```

---

### PUT /api/v1/admin/users/{userId}/status
**Description:** Update user status (activate, suspend, deactivate)

**Request:**
```json
{
  "status": "ACTIVE|INACTIVE|SUSPENDED"
}
```

**Response:** 200 OK
```json
{
  "message": "User status updated successfully"
}
```

---

### GET /api/v1/admin/rides
**Description:** Get all rides with filters

**Query Parameters:**
- `status`: string (PENDING|ACCEPTED|IN_PROGRESS|COMPLETED|CANCELLED)
- `startDate`: string (ISO date)
- `endDate`: string (ISO date)
- `page`: number (default: 0)
- `size`: number (default: 20)

**Response:** 200 OK
```json
{
  "rides": [
    {
      "rideId": "string",
      "riderId": "string",
      "riderName": "string",
      "driverId": "string",
      "driverName": "string",
      "pickupLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "dropoffLocation": {
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      },
      "status": "PENDING|ACCEPTED|IN_PROGRESS|COMPLETED|CANCELLED",
      "fare": 0.0,
      "createdAt": "2024-01-01T00:00:00Z",
      "completedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 0,
  "totalPages": 0
}
```

---

### GET /api/v1/admin/rides/{rideId}
**Description:** Get ride details (admin view)

**Response:** 200 OK
```json
{
  "rideId": "string",
  "riderId": "string",
  "riderName": "string",
  "riderEmail": "string",
  "driverId": "string",
  "driverName": "string",
  "driverEmail": "string",
  "pickupLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "dropoffLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "status": "PENDING|ACCEPTED|IN_PROGRESS|COMPLETED|CANCELLED",
  "fare": 0.0,
  "createdAt": "2024-01-01T00:00:00Z",
  "acceptedAt": "2024-01-01T00:00:00Z",
  "startedAt": "2024-01-01T00:00:00Z",
  "completedAt": "2024-01-01T00:00:00Z"
}
```

---

### GET /api/v1/admin/statistics
**Description:** Get platform statistics

**Query Parameters:**
- `startDate`: string (ISO date)
- `endDate`: string (ISO date)

**Response:** 200 OK
```json
{
  "totalRides": 0,
  "completedRides": 0,
  "cancelledRides": 0,
  "totalRevenue": 0.0,
  "totalDrivers": 0,
  "activeDrivers": 0,
  "totalRiders": 0,
  "period": {
    "startDate": "2024-01-01T00:00:00Z",
    "endDate": "2024-01-31T23:59:59Z"
  }
}
```

---

## WebSocket Event Contracts

### Connection
**Endpoint:** `wss://domain.com/ws`

**Authentication:** JWT token passed as query parameter: `?token=<jwt_token>`

**Connection Events:**

---

### Client → Server Events

#### Location Update
**Event Name:** `location:update`
**From:** Driver app
**Payload:**
```json
{
  "latitude": 0.0,
  "longitude": 0.0,
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

#### Heartbeat
**Event Name:** `ping`
**From:** All clients
**Payload:**
```json
{
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

### Server → Client Events

#### Ride Request
**Event Name:** `ride:request`
**To:** Driver app
**Payload:**
```json
{
  "rideId": "string",
  "riderName": "string",
  "pickupLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "dropoffLocation": {
    "latitude": 0.0,
    "longitude": 0.0,
    "address": "string"
  },
  "estimatedFare": 0.0,
  "estimatedDistance": 0.0
}
```

---

#### Ride Status Update
**Event Name:** `ride:status:updated`
**To:** Rider app, Driver app
**Payload:**
```json
{
  "rideId": "string",
  "status": "PENDING|ACCEPTED|IN_PROGRESS|COMPLETED|CANCELLED",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

#### Driver Location Update
**Event Name:** `ride:driver:location`
**To:** Rider app (during active ride)
**Payload:**
```json
{
  "rideId": "string",
  "driverLocation": {
    "latitude": 0.0,
    "longitude": 0.0
  },
  "estimatedArrivalTime": 5,
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

#### Driver Assigned
**Event Name:** `ride:driver:assigned`
**To:** Rider app
**Payload:**
```json
{
  "rideId": "string",
  "driverId": "string",
  "driverName": "string",
  "driverRating": 4.5,
  "vehicleModel": "string",
  "vehiclePlate": "string",
  "driverLocation": {
    "latitude": 0.0,
    "longitude": 0.0
  },
  "estimatedArrivalTime": 5
}
```

---

#### Ride Cancelled
**Event Name:** `ride:cancelled`
**To:** Rider app, Driver app
**Payload:**
```json
{
  "rideId": "string",
  "cancelledBy": "RIDER|DRIVER",
  "reason": "string",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

#### Ride Completed
**Event Name:** `ride:completed`
**To:** Rider app, Driver app
**Payload:**
```json
{
  "rideId": "string",
  "fare": 0.0,
  "completedAt": "2024-01-01T00:00:00Z"
}
```

---

#### Notification
**Event Name:** `notification`
**To:** All clients
**Payload:**
```json
{
  "type": "INFO|WARNING|ERROR",
  "title": "string",
  "message": "string",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

#### Heartbeat Response
**Event Name:** `pong`
**To:** All clients
**Payload:**
```json
{
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

## Error Responses

### Standard Error Format
**Status Code:** 4xx or 5xx

**Response:**
```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message",
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

### Common Error Codes
- `UNAUTHORIZED` (401): Invalid or missing authentication token
- `FORBIDDEN` (403): Insufficient permissions
- `NOT_FOUND` (404): Resource not found
- `BAD_REQUEST` (400): Invalid request data
- `CONFLICT` (409): Resource conflict (e.g., ride already accepted)
- `INTERNAL_ERROR` (500): Server error

---

## Status Codes Summary

### Ride Status Values
- `PENDING`: Ride request created, awaiting driver acceptance
- `ACCEPTED`: Driver accepted the ride request
- `IN_PROGRESS`: Driver started the ride (picked up rider)
- `COMPLETED`: Ride completed successfully
- `CANCELLED`: Ride cancelled by rider or driver

### Driver Status Values
- `AVAILABLE`: Driver is available for ride requests
- `BUSY`: Driver is currently on a ride
- `OFFLINE`: Driver is offline/unavailable

### User Status Values
- `ACTIVE`: User account is active
- `INACTIVE`: User account is inactive
- `SUSPENDED`: User account is suspended by admin

