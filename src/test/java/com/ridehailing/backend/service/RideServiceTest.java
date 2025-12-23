package com.ridehailing.backend.service;

import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private WebSocketEventService webSocketEventService;

    private RideService rideService;

    private UUID riderId;
    private UUID driverId;
    private UUID vehicleId;
    private UUID rideId;

    @BeforeEach
    void setUp() {
        rideService = new RideService(rideRepository, webSocketEventService);
        riderId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        vehicleId = UUID.randomUUID();
        rideId = UUID.randomUUID();
    }

    @Test
    void createRide_ShouldCreateRideWithPendingStatus() {
        BigDecimal pickupLat = new BigDecimal("40.7128");
        BigDecimal pickupLng = new BigDecimal("-74.0060");
        String pickupAddr = "123 Main St";
        BigDecimal dropoffLat = new BigDecimal("40.7589");
        BigDecimal dropoffLng = new BigDecimal("-73.9851");
        String dropoffAddr = "456 Park Ave";

        Ride savedRide = new Ride();
        savedRide.setRideId(rideId);
        savedRide.setRiderId(riderId);
        savedRide.setStatus(RideStatus.PENDING);

        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        Ride result = rideService.createRide(riderId, pickupLat, pickupLng, pickupAddr,
                dropoffLat, dropoffLng, dropoffAddr);

        assertNotNull(result);
        assertEquals(RideStatus.PENDING, result.getStatus());
        assertEquals(riderId, result.getRiderId());
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void acceptRide_ShouldUpdateStatusToAccepted() {
        Ride ride = createPendingRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.acceptRide(rideId, driverId, vehicleId);

        assertEquals(RideStatus.ACCEPTED, result.getStatus());
        assertEquals(driverId, result.getDriverId());
        assertEquals(vehicleId, result.getVehicleId());
        assertNotNull(result.getAcceptedAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void acceptRide_WhenRideNotFound_ShouldThrowException() {
        when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            rideService.acceptRide(rideId, driverId, vehicleId);
        });
    }

    @Test
    void acceptRide_WhenNotPending_ShouldThrowException() {
        Ride ride = createAcceptedRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> {
            rideService.acceptRide(rideId, driverId, vehicleId);
        });
    }

    @Test
    void startRide_ShouldUpdateStatusToInProgress() {
        Ride ride = createAcceptedRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.startRide(rideId, driverId);

        assertEquals(RideStatus.IN_PROGRESS, result.getStatus());
        assertNotNull(result.getStartedAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void startRide_WhenNotAccepted_ShouldThrowException() {
        Ride ride = createPendingRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> {
            rideService.startRide(rideId, driverId);
        });
    }

    @Test
    void completeRide_ShouldUpdateStatusToCompleted() {
        Ride ride = createInProgressRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.completeRide(rideId, driverId);

        assertEquals(RideStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getCompletedAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void completeRide_WhenNotInProgress_ShouldThrowException() {
        Ride ride = createAcceptedRide();
        when(rideRepository.findByRideIdAndDriverId(rideId, driverId)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> {
            rideService.completeRide(rideId, driverId);
        });
    }

    @Test
    void cancelRide_ByRider_ShouldUpdateStatusToCancelled() {
        Ride ride = createPendingRide();
        String reason = "Change of plans";
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.cancelRide(rideId, riderId, true, reason);

        assertEquals(RideStatus.CANCELLED, result.getStatus());
        assertEquals("RIDER", result.getCancelledBy());
        assertEquals(reason, result.getCancellationReason());
        assertNotNull(result.getCancelledAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void cancelRide_ByDriver_ShouldUpdateStatusToCancelled() {
        Ride ride = createAcceptedRide();
        String reason = "Unable to reach location";
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.cancelRide(rideId, driverId, false, reason);

        assertEquals(RideStatus.CANCELLED, result.getStatus());
        assertEquals("DRIVER", result.getCancelledBy());
        assertEquals(reason, result.getCancellationReason());
        assertNotNull(result.getCancelledAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void cancelRide_WhenCompleted_ShouldThrowException() {
        Ride ride = createCompletedRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> {
            rideService.cancelRide(rideId, riderId, true, "Reason");
        });
    }

    @Test
    void cancelRide_WhenAlreadyCancelled_ShouldThrowException() {
        Ride ride = createCancelledRide();
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> {
            rideService.cancelRide(rideId, riderId, true, "Reason");
        });
    }

    @Test
    void findByRiderId_ShouldReturnRides() {
        List<Ride> rides = new ArrayList<>();
        rides.add(createPendingRide());
        when(rideRepository.findByRiderId(riderId)).thenReturn(rides);

        List<Ride> result = rideService.findByRiderId(riderId);

        assertEquals(1, result.size());
        verify(rideRepository).findByRiderId(riderId);
    }

    @Test
    void findByRiderIdAndStatus_ShouldReturnFilteredRides() {
        List<Ride> rides = new ArrayList<>();
        rides.add(createPendingRide());
        when(rideRepository.findByRiderIdAndStatus(riderId, RideStatus.PENDING)).thenReturn(rides);

        List<Ride> result = rideService.findByRiderIdAndStatus(riderId, RideStatus.PENDING);

        assertEquals(1, result.size());
        verify(rideRepository).findByRiderIdAndStatus(riderId, RideStatus.PENDING);
    }

    private Ride createPendingRide() {
        Ride ride = new Ride();
        ride.setRideId(rideId);
        ride.setRiderId(riderId);
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

    private Ride createCompletedRide() {
        Ride ride = createInProgressRide();
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        return ride;
    }

    private Ride createCancelledRide() {
        Ride ride = createPendingRide();
        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledBy("RIDER");
        ride.setCancelledAt(LocalDateTime.now());
        return ride;
    }
}

