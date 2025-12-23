package com.ridehailing.backend.websocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class DriverLocationUpdateEvent {
    private UUID rideId;
    private Location driverLocation;
    private Integer estimatedArrivalTime; // in minutes
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;

    public DriverLocationUpdateEvent() {
    }

    public DriverLocationUpdateEvent(UUID rideId, Location driverLocation, Integer estimatedArrivalTime, LocalDateTime timestamp) {
        this.rideId = rideId;
        this.driverLocation = driverLocation;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.timestamp = timestamp;
    }

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public Location getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(Location driverLocation) {
        this.driverLocation = driverLocation;
    }

    public Integer getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(Integer estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class Location {
        private Double latitude;
        private Double longitude;

        public Location() {
        }

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
}

