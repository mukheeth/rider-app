package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.Ride;
import com.ridehailing.backend.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {
    List<Ride> findByRiderId(UUID riderId);
    List<Ride> findByDriverId(UUID driverId);
    List<Ride> findByRiderIdAndStatus(UUID riderId, RideStatus status);
    List<Ride> findByDriverIdAndStatus(UUID driverId, RideStatus status);
    Optional<Ride> findByRideIdAndRiderId(UUID rideId, UUID riderId);
    Optional<Ride> findByRideIdAndDriverId(UUID rideId, UUID driverId);
}

