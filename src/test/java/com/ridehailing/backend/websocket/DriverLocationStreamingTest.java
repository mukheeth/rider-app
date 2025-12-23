package com.ridehailing.backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.service.RideService;
import com.ridehailing.backend.websocket.dto.DriverLocationUpdateEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "jwt.secret=mySecretKeyForJWTTokenGenerationThatMustBeAtLeast256BitsLongForHS256Algorithm",
        "jwt.expiration=3600000"
})
class DriverLocationStreamingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RideService rideService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void driverLocationUpdate_ShouldBeSentToRider() throws Exception {
        // Create a ride with driver
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();

        Ride ride = rideService.createRide(
                riderId,
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060"),
                "123 Main St",
                new BigDecimal("40.7580"),
                new BigDecimal("-73.9855"),
                "456 Broadway"
        );

        // Accept the ride so driver is assigned
        rideService.acceptRide(ride.getRideId(), driverId, vehicleId);

        // Connect rider WebSocket
        CompletableFuture<String> riderMessageFuture = new CompletableFuture<>();
        String riderWsUrl = "ws://localhost:" + port + "/ws?userId=" + riderId;
        
        WebSocketSession riderSession = new StandardWebSocketClient().doHandshake(
                new org.springframework.web.socket.WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) {
                        // Connection established
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) {
                        if (message instanceof TextMessage) {
                            String payload = ((TextMessage) message).getPayload();
                            // Only capture JSON messages (location updates)
                            if (payload.startsWith("{")) {
                                riderMessageFuture.complete(payload);
                            }
                        }
                    }

                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) {
                        riderMessageFuture.completeExceptionally(exception);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) {
                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, riderWsUrl).get();

        // Wait a bit for rider connection to be established
        Thread.sleep(200);

        // Connect driver WebSocket and send location update
        String driverWsUrl = "ws://localhost:" + port + "/ws?userId=" + driverId;
        
        WebSocketSession driverSession = new StandardWebSocketClient().doHandshake(
                new org.springframework.web.socket.WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) {
                        try {
                            // Wait for connection to be fully established and userId to be registered
                            Thread.sleep(300);
                            String locationUpdate = "{\"latitude\":40.7580,\"longitude\":-73.9855}";
                            session.sendMessage(new TextMessage(locationUpdate));
                        } catch (Exception e) {
                            riderMessageFuture.completeExceptionally(e);
                        }
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) {
                        // Driver doesn't need to handle messages for this test
                    }

                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) {
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) {
                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, driverWsUrl).get();

        // Wait for location update to be received by rider
        String receivedMessage = riderMessageFuture.get(5, TimeUnit.SECONDS);
        assertNotNull(receivedMessage, "Rider should receive location update");

        // Parse and verify the location update event
        DriverLocationUpdateEvent event = objectMapper.readValue(receivedMessage, DriverLocationUpdateEvent.class);
        assertEquals(ride.getRideId(), event.getRideId());
        assertNotNull(event.getDriverLocation());
        assertEquals(40.7580, event.getDriverLocation().getLatitude());
        assertEquals(-73.9855, event.getDriverLocation().getLongitude());
        assertNotNull(event.getTimestamp());

        // Cleanup
        riderSession.close();
        driverSession.close();
    }
}

