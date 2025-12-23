package com.ridehailing.backend.service;

import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import com.ridehailing.backend.repository.RideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RideService {

    private final RideRepository rideRepository;
    private final WebSocketEventService webSocketEventService;

    public RideService(RideRepository rideRepository, WebSocketEventService webSocketEventService) {
        this.rideRepository = rideRepository;
        this.webSocketEventService = webSocketEventService;
    }

    public Ride createRide(UUID riderId, BigDecimal pickupLatitude, BigDecimal pickupLongitude,
                           String pickupAddress, BigDecimal dropoffLatitude, BigDecimal dropoffLongitude,
                           String dropoffAddress) {
        Ride ride = new Ride();
        ride.setRiderId(riderId);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setPickupAddress(pickupAddress);
        ride.setDropoffLatitude(dropoffLatitude);
        ride.setDropoffLongitude(dropoffLongitude);
        ride.setDropoffAddress(dropoffAddress);
        ride.setStatus(RideStatus.PENDING);
        
        return rideRepository.save(ride);
    }

    public Optional<Ride> findById(UUID rideId) {
        return rideRepository.findById(rideId);
    }

    public List<Ride> findByRiderId(UUID riderId) {
        return rideRepository.findByRiderId(riderId);
    }

    public List<Ride> findByDriverId(UUID driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    public List<Ride> findByRiderIdAndStatus(UUID riderId, RideStatus status) {
        return rideRepository.findByRiderIdAndStatus(riderId, status);
    }

    public List<Ride> findByDriverIdAndStatus(UUID driverId, RideStatus status) {
        return rideRepository.findByDriverIdAndStatus(driverId, status);
    }

    public Ride acceptRide(UUID rideId, UUID driverId, UUID vehicleId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        if (ride.getStatus() != RideStatus.PENDING) {
            throw new IllegalStateException("Ride cannot be accepted. Current status: " + ride.getStatus());
        }

        ride.setDriverId(driverId);
        ride.setVehicleId(vehicleId);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        webSocketEventService.emitRideStatusUpdate(savedRide.getRideId(), savedRide.getStatus());
        return savedRide;
    }

    public Ride startRide(UUID rideId, UUID driverId) {
        Ride ride = rideRepository.findByRideIdAndDriverId(rideId, driverId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found or not assigned to driver"));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new IllegalStateException("Ride cannot be started. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        webSocketEventService.emitRideStatusUpdate(savedRide.getRideId(), savedRide.getStatus());
        return savedRide;
    }

    public Ride completeRide(UUID rideId, UUID driverId) {
        Ride ride = rideRepository.findByRideIdAndDriverId(rideId, driverId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found or not assigned to driver"));

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ride cannot be completed. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        webSocketEventService.emitRideStatusUpdate(savedRide.getRideId(), savedRide.getStatus());
        return savedRide;
    }

    public Ride cancelRide(UUID rideId, UUID userId, boolean isRider, String reason) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed ride");
        }

        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new IllegalStateException("Ride is already cancelled");
        }

        if (isRider && !ride.getRiderId().equals(userId)) {
            throw new IllegalArgumentException("Rider does not own this ride");
        }

        if (!isRider && !ride.getDriverId().equals(userId)) {
            throw new IllegalArgumentException("Driver does not own this ride");
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledBy(isRider ? "RIDER" : "DRIVER");
        ride.setCancellationReason(reason);
        ride.setCancelledAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        webSocketEventService.emitRideStatusUpdate(savedRide.getRideId(), savedRide.getStatus());
        return savedRide;
    }
}

