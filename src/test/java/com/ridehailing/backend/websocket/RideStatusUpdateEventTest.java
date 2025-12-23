package com.ridehailing.backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.service.RideService;
import com.ridehailing.backend.service.WebSocketEventService;
import com.ridehailing.backend.websocket.dto.RideStatusUpdateEvent;
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
class RideStatusUpdateEventTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RideService rideService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void rideStatusChange_ShouldEmitWebSocketEvent() throws Exception {
        // Create a ride
        UUID riderId = UUID.randomUUID();
        Ride ride = rideService.createRide(
                riderId,
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060"),
                "123 Main St",
                new BigDecimal("40.7580"),
                new BigDecimal("-73.9855"),
                "456 Broadway"
        );

        UUID driverId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();

        // Connect WebSocket client
        StandardWebSocketClient client = new StandardWebSocketClient();
        CompletableFuture<String> messageFuture = new CompletableFuture<>();

        String wsUrl = "ws://localhost:" + port + "/ws";

        WebSocketSession session = client.doHandshake(new org.springframework.web.socket.WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                // Connection established, now trigger status change
                try {
                    Thread.sleep(100); // Give time for connection to be fully established
                    rideService.acceptRide(ride.getRideId(), driverId, vehicleId);
                } catch (Exception e) {
                    messageFuture.completeExceptionally(e);
                }
            }

            @Override
            public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) {
                if (message instanceof TextMessage) {
                    String payload = ((TextMessage) message).getPayload();
                    // Ignore echo messages, only process JSON events
                    if (payload.startsWith("{")) {
                        messageFuture.complete(payload);
                    }
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                messageFuture.completeExceptionally(exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) {
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        }, wsUrl).get();

        // Wait for event message
        String eventMessage = messageFuture.get(5, TimeUnit.SECONDS);
        assertNotNull(eventMessage, "Should receive WebSocket event");

        // Parse and verify event
        RideStatusUpdateEvent event = objectMapper.readValue(eventMessage, RideStatusUpdateEvent.class);
        assertEquals(ride.getRideId(), event.getRideId());
        assertEquals(RideStatus.ACCEPTED, event.getStatus());
        assertNotNull(event.getTimestamp());

        session.close();
    }
}

