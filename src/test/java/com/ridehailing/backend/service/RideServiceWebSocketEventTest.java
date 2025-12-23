package com.ridehailing.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.websocket.WebSocketHandler;
import com.ridehailing.backend.websocket.dto.RideStatusUpdateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceWebSocketEventTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private WebSocketEventService webSocketEventService;

    private RideService rideService;
    private ObjectMapper objectMapper;

    private UUID riderId;
    private UUID driverId;
    private UUID vehicleId;
    private UUID rideId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        rideService = new RideService(rideRepository, webSocketEventService);
        
        riderId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();
        rideId = UUID.randomUUID();
    }

    @Test
    void acceptRide_ShouldEmitStatusUpdateEvent() {
        Ride ride = createPendingRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        
        Ride savedRide = createPendingRide();
        savedRide.setDriverId(driverId);
        savedRide.setVehicleId(vehicleId);
        savedRide.setStatus(RideStatus.ACCEPTED);
        savedRide.setAcceptedAt(LocalDateTime.now());
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        rideService.acceptRide(rideId, driverId, vehicleId);

        ArgumentCaptor<UUID> rideIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<RideStatus> statusCaptor = ArgumentCaptor.forClass(RideStatus.class);
        verify(webSocketEventService, times(1)).emitRideStatusUpdate(rideIdCaptor.capture(), statusCaptor.capture());
        
        assertEquals(rideId, rideIdCaptor.getValue());
        assertEquals(RideStatus.ACCEPTED, statusCaptor.getValue());
    }

    @Test
    void startRide_ShouldEmitStatusUpdateEvent() {
        Ride ride = createAcceptedRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));
        
        Ride savedRide = createAcceptedRide();
        savedRide.setStatus(RideStatus.IN_PROGRESS);
        savedRide.setStartedAt(LocalDateTime.now());
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        rideService.startRide(rideId, driverId);

        ArgumentCaptor<UUID> rideIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<RideStatus> statusCaptor = ArgumentCaptor.forClass(RideStatus.class);
        verify(webSocketEventService, times(1)).emitRideStatusUpdate(rideIdCaptor.capture(), statusCaptor.capture());
        
        assertEquals(rideId, rideIdCaptor.getValue());
        assertEquals(RideStatus.IN_PROGRESS, statusCaptor.getValue());
    }

    @Test
    void completeRide_ShouldEmitStatusUpdateEvent() {
        Ride ride = createInProgressRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));
        
        Ride savedRide = createInProgressRide();
        savedRide.setStatus(RideStatus.COMPLETED);
        savedRide.setCompletedAt(LocalDateTime.now());
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        rideService.completeRide(rideId, driverId);

        ArgumentCaptor<UUID> rideIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<RideStatus> statusCaptor = ArgumentCaptor.forClass(RideStatus.class);
        verify(webSocketEventService, times(1)).emitRideStatusUpdate(rideIdCaptor.capture(), statusCaptor.capture());
        
        assertEquals(rideId, rideIdCaptor.getValue());
        assertEquals(RideStatus.COMPLETED, statusCaptor.getValue());
    }

    @Test
    void cancelRide_ShouldEmitStatusUpdateEvent() {
        Ride ride = createPendingRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        
        Ride savedRide = createPendingRide();
        savedRide.setStatus(RideStatus.CANCELLED);
        savedRide.setCancelledBy("RIDER");
        savedRide.setCancellationReason("Change of plans");
        savedRide.setCancelledAt(LocalDateTime.now());
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        rideService.cancelRide(rideId, riderId, true, "Change of plans");

        ArgumentCaptor<UUID> rideIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<RideStatus> statusCaptor = ArgumentCaptor.forClass(RideStatus.class);
        verify(webSocketEventService, times(1)).emitRideStatusUpdate(rideIdCaptor.capture(), statusCaptor.capture());
        
        assertEquals(rideId, rideIdCaptor.getValue());
        assertEquals(RideStatus.CANCELLED, statusCaptor.getValue());
    }


    private Ride createPendingRide() {
        Ride ride = new Ride();
        ride.setRideId(rideId);
        ride.setRiderId(riderId);
        ride.setPickupLatitude(new BigDecimal("40.7128"));
        ride.setPickupLongitude(new BigDecimal("-74.0060"));
        ride.setPickupAddress("123 Main St");
        ride.setStatus(RideStatus.PENDING);
        ride.setCreatedAt(LocalDateTime.now());
        return ride;
    }

    private Ride createAcceptedRide() {
        Ride ride = createPendingRide();
        ride.setDriverId(driverId);
        ride.setVehicleId(vehicleId);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        return ride;
    }

    private Ride createInProgressRide() {
        Ride ride = createAcceptedRide();
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());
        return ride;
    }
}

