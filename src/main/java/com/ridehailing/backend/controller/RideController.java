package com.ridehailing.backend.controller;

import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRide(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        
        try {
            // Extract rider ID from authentication
            String riderIdStr = authentication.getName();
            UUID riderId = UUID.fromString(riderIdStr);

            // Extract pickup location
            @SuppressWarnings("unchecked")
            Map<String, Object> pickupLocation = (Map<String, Object>) request.get("pickupLocation");
            BigDecimal pickupLatitude = new BigDecimal(pickupLocation.get("latitude").toString());
            BigDecimal pickupLongitude = new BigDecimal(pickupLocation.get("longitude").toString());
            String pickupAddress = pickupLocation.get("address").toString();

            // Extract dropoff location
            @SuppressWarnings("unchecked")
            Map<String, Object> dropoffLocation = (Map<String, Object>) request.get("dropoffLocation");
            BigDecimal dropoffLatitude = new BigDecimal(dropoffLocation.get("latitude").toString());
            BigDecimal dropoffLongitude = new BigDecimal(dropoffLocation.get("longitude").toString());
            String dropoffAddress = dropoffLocation.get("address").toString();

            // Create ride
            Ride ride = rideService.createRide(
                    riderId,
                    pickupLatitude,
                    pickupLongitude,
                    pickupAddress,
                    dropoffLatitude,
                    dropoffLongitude,
                    dropoffAddress
            );

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("rideId", ride.getRideId().toString());
            response.put("status", ride.getStatus().name());
            response.put("estimatedFare", calculateEstimatedFare(pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude));
            response.put("estimatedArrivalTime", 5); // Mock: 5 minutes

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create ride");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Map<String, Object>> getRideDetails(@PathVariable UUID rideId) {
        return rideService.findById(rideId)
                .map(ride -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("rideId", ride.getRideId().toString());
                    response.put("riderId", ride.getRiderId().toString());
                    if (ride.getDriverId() != null) {
                        response.put("driverId", ride.getDriverId().toString());
                    }
                    
                    Map<String, Object> pickupLocation = new HashMap<>();
                    pickupLocation.put("latitude", ride.getPickupLatitude());
                    pickupLocation.put("longitude", ride.getPickupLongitude());
                    pickupLocation.put("address", ride.getPickupAddress());
                    response.put("pickupLocation", pickupLocation);
                    
                    Map<String, Object> dropoffLocation = new HashMap<>();
                    dropoffLocation.put("latitude", ride.getDropoffLatitude());
                    dropoffLocation.put("longitude", ride.getDropoffLongitude());
                    dropoffLocation.put("address", ride.getDropoffAddress());
                    response.put("dropoffLocation", dropoffLocation);
                    
                    response.put("status", ride.getStatus().name());
                    // Calculate fare if ride is completed
                    if (ride.getStatus() == RideStatus.COMPLETED) {
                        response.put("fare", calculateEstimatedFare(
                            ride.getPickupLatitude(), ride.getPickupLongitude(),
                            ride.getDropoffLatitude(), ride.getDropoffLongitude()
                        ));
                    } else {
                        response.put("fare", 0.0);
                    }
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper method to calculate estimated fare (mock implementation)
    private double calculateEstimatedFare(BigDecimal pickupLat, BigDecimal pickupLon,
                                         BigDecimal dropoffLat, BigDecimal dropoffLon) {
        // Simple distance calculation (Haversine formula approximation)
        double lat1 = pickupLat.doubleValue();
        double lon1 = pickupLon.doubleValue();
        double lat2 = dropoffLat.doubleValue();
        double lon2 = dropoffLon.doubleValue();
        
        double distance = Math.sqrt(
            Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2)
        ) * 111; // Rough conversion to km
        
        // Base fare + distance-based fare
        double baseFare = 2.50;
        double perKmFare = 1.50;
        
        return Math.round((baseFare + (distance * perKmFare)) * 100.0) / 100.0;
    }
}

