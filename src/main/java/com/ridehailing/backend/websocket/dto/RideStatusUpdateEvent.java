package com.ridehailing.backend.websocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ridehailing.backend.model.RideStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class RideStatusUpdateEvent {
    private UUID rideId;
    private RideStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;

    public RideStatusUpdateEvent() {
    }

    public RideStatusUpdateEvent(UUID rideId, RideStatus status, LocalDateTime timestamp) {
        this.rideId = rideId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

