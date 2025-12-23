package com.ridehailing.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.websocket.WebSocketHandler;
import com.ridehailing.backend.websocket.dto.DriverLocationUpdateEvent;
import com.ridehailing.backend.websocket.dto.LocationUpdateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {

    private final RideRepository rideRepository;
    private final WebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public LocationService(RideRepository rideRepository, WebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.rideRepository = rideRepository;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void handleDriverLocationUpdate(UUID driverId, LocationUpdateRequest locationUpdate) {
        // Find active ride for this driver
        Optional<Ride> activeRide = rideRepository.findByDriverIdAndStatus(driverId, RideStatus.ACCEPTED)
                .stream()
                .findFirst()
                .or(() -> rideRepository.findByDriverIdAndStatus(driverId, RideStatus.IN_PROGRESS)
                        .stream()
                        .findFirst());

        if (activeRide.isPresent()) {
            Ride ride = activeRide.get();
            UUID riderId = ride.getRiderId();

            // Create location update event
            DriverLocationUpdateEvent.Location driverLocation = 
                    new DriverLocationUpdateEvent.Location(locationUpdate.getLatitude(), locationUpdate.getLongitude());
            
            // For now, set estimated arrival time to null or a default value
            // In production, this would be calculated based on distance and traffic
            DriverLocationUpdateEvent event = new DriverLocationUpdateEvent(
                    ride.getRideId(),
                    driverLocation,
                    null, // estimatedArrivalTime - can be calculated later
                    locationUpdate.getTimestamp() != null ? locationUpdate.getTimestamp() : LocalDateTime.now()
            );

            try {
                String jsonMessage = objectMapper.writeValueAsString(event);
                // Send to rider
                webSocketHandler.sendToUser(riderId.toString(), jsonMessage);
            } catch (JsonProcessingException e) {
                // Log error, but don't fail
            }
        }
    }
}

