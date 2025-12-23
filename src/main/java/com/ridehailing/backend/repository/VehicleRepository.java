package com.ridehailing.backend.repository;

import com.ridehailing.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByVehicleId(UUID vehicleId);
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    List<Vehicle> findByDriver_DriverId(UUID driverId);
    
    @Query("SELECT v FROM Vehicle v WHERE v.driver.driverId = :driverId AND v.isActive = true")
    List<Vehicle> findActiveVehiclesByDriverId(@Param("driverId") UUID driverId);
}

