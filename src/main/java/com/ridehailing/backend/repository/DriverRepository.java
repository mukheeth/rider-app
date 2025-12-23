package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    Optional<Driver> findByDriverId(UUID driverId);
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    
    @Modifying
    @Query(value = "INSERT INTO drivers (driver_id, license_number, license_expiry, total_rides, total_earnings, status, is_verified, created_at, updated_at) VALUES (:driverId, :licenseNumber, :licenseExpiry, 0, 0.00, 'OFFLINE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
    void insertDriver(@Param("driverId") UUID driverId, @Param("licenseNumber") String licenseNumber, @Param("licenseExpiry") LocalDate licenseExpiry);
}

