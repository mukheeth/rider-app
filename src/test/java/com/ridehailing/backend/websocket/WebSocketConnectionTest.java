package com.ridehailing.backend.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

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
class WebSocketConnectionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Test
    void testWebSocketConnection() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();
        CompletableFuture<String> messageFuture = new CompletableFuture<>();
        CompletableFuture<Boolean> connectionFuture = new CompletableFuture<>();

        String wsUrl = "ws://localhost:" + port + "/ws";

        WebSocketSession session = client.doHandshake(new org.springframework.web.socket.WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                connectionFuture.complete(true);
            }

            @Override
            public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) {
                if (message instanceof TextMessage) {
                    messageFuture.complete(((TextMessage) message).getPayload());
                }
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                connectionFuture.completeExceptionally(exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) {
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        }, wsUrl).get();

        // Wait for connection
        assertTrue(connectionFuture.get(5, TimeUnit.SECONDS), "WebSocket connection should be established");

        // Verify handler has the session
        assertTrue(webSocketHandler.getActiveConnections() > 0, "Handler should track active connections");

        // Send a test message
        session.sendMessage(new TextMessage("test message"));

        // Wait for echo response
        String response = messageFuture.get(5, TimeUnit.SECONDS);
        assertNotNull(response, "Should receive echo response");
        assertTrue(response.contains("Echo: test message"), "Response should contain echo");

        // Close connection
        session.close();
        Thread.sleep(200); // Give time for cleanup

        // Verify connection is removed
        assertEquals(0, webSocketHandler.getActiveConnections(), "Handler should remove closed connections");
    }
}

