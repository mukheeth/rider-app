package com.ridehailing.backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.service.LocationService;
import com.ridehailing.backend.util.JwtUtil;
import com.ridehailing.backend.websocket.dto.LocationUpdateRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String LOCATION_UPDATE_EVENT = "location:update";

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, WebSocketSession> sessionsByUserId = new ConcurrentHashMap<>();
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final LocationService locationService;

    public WebSocketHandler(JwtUtil jwtUtil, ObjectMapper objectMapper, @Lazy LocationService locationService) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.locationService = locationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        
        // Extract user ID from query parameters (token can be passed as query param)
        String userId = extractUserIdFromSession(session);
        if (userId != null) {
            session.getAttributes().put(USER_ID_ATTRIBUTE, userId);
            sessionsByUserId.put(userId, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        String userId = (String) session.getAttributes().get(USER_ID_ATTRIBUTE);
        if (userId != null) {
            sessionsByUserId.remove(userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        
        try {
            // Try to parse as JSON to check if it's a structured message
            if (payload.trim().startsWith("{")) {
                // Try to parse as location update
                LocationUpdateRequest locationUpdate = objectMapper.readValue(payload, LocationUpdateRequest.class);
                
                // Check if it's a location update (has latitude and longitude)
                if (locationUpdate.getLatitude() != null && locationUpdate.getLongitude() != null) {
                    String userId = (String) session.getAttributes().get(USER_ID_ATTRIBUTE);
                    if (userId != null) {
                        UUID driverId = UUID.fromString(userId);
                        locationService.handleDriverLocationUpdate(driverId, locationUpdate);
                    }
                    return;
                }
            }
            
            // Default: echo back for connection test
            session.sendMessage(new TextMessage("Echo: " + payload));
        } catch (Exception e) {
            // If parsing fails, echo back for backward compatibility
            session.sendMessage(new TextMessage("Echo: " + payload));
        }
    }

    public int getActiveConnections() {
        return sessions.size();
    }

    public void broadcast(String message) {
        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                // Log error, but continue with other sessions
            }
        });
    }

    public void sendToUser(String userId, String message) {
        WebSocketSession session = sessionsByUserId.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                // Log error
            }
        }
    }

    private String extractUserIdFromSession(WebSocketSession session) {
        // Try to get userId from query parameters (e.g., ?userId=123)
        URI uri = session.getUri();
        if (uri != null && uri.getQuery() != null) {
            String query = uri.getQuery();
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}

