package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiderRepository extends JpaRepository<Rider, UUID> {
    Optional<Rider> findByRiderId(UUID riderId);
    
    @Modifying
    @Query(value = "INSERT INTO riders (rider_id, total_rides, created_at, updated_at) VALUES (:riderId, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
    void insertRider(@Param("riderId") UUID riderId);
}

