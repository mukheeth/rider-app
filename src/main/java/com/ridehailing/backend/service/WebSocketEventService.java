package com.ridehailing.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.websocket.WebSocketHandler;
import com.ridehailing.backend.websocket.dto.RideStatusUpdateEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WebSocketEventService {

    private final WebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public WebSocketEventService(WebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void emitRideStatusUpdate(UUID rideId, RideStatus status) {
        RideStatusUpdateEvent event = new RideStatusUpdateEvent(rideId, status, LocalDateTime.now());
        try {
            String jsonMessage = objectMapper.writeValueAsString(event);
            webSocketHandler.broadcast(jsonMessage);
        } catch (JsonProcessingException e) {
            // Log error, but don't fail the ride status update
        }
    }
}

