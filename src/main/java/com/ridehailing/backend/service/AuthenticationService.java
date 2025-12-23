package com.ridehailing.backend.service;

import com.ridehailing.backend.entity.RoleEntity;
import com.ridehailing.backend.entity.User;
import com.ridehailing.backend.entity.Vehicle;
import com.ridehailing.backend.model.Role;
import com.ridehailing.backend.repository.DriverRepository;
import com.ridehailing.backend.repository.RiderRepository;
import com.ridehailing.backend.repository.RoleRepository;
import com.ridehailing.backend.repository.UserRepository;
import com.ridehailing.backend.repository.VehicleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RiderRepository riderRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.riderRepository = riderRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new rider
     */
    public User registerRider(String email, String password, String phoneNumber,
                              String firstName, String lastName) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        // Get RIDER role
        RoleEntity riderRole = roleRepository.findByRoleName("RIDER")
                .orElseThrow(() -> new IllegalStateException("RIDER role not found in database"));

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password)); // Hash password
        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(riderRole);
        user.setStatus("ACTIVE");

        // Save user first - this will generate the UUID
        User savedUser = userRepository.save(user);
        
        // Flush to ensure user is persisted and ID is available
        userRepository.flush();
        
        // Verify user has an ID
        UUID userId = savedUser.getUserId();
        if (userId == null) {
            throw new IllegalStateException("User ID was not generated after save");
        }

        // Create rider record using native query to avoid Hibernate relationship issues
        riderRepository.insertRider(userId);

        return savedUser;
    }

    /**
     * Register a new driver
     */
    public User registerDriver(String email, String password, String phoneNumber,
                              String firstName, String lastName, String licenseNumber,
                              String vehicleModel, String vehiclePlate) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        // Check if license number already exists
        if (driverRepository.findByLicenseNumber(licenseNumber).isPresent()) {
            throw new IllegalArgumentException("License number already registered");
        }

        // Check if license plate already exists
        if (vehicleRepository.findByLicensePlate(vehiclePlate).isPresent()) {
            throw new IllegalArgumentException("License plate already registered");
        }

        // Get DRIVER role
        RoleEntity driverRole = roleRepository.findByRoleName("DRIVER")
                .orElseThrow(() -> new IllegalStateException("DRIVER role not found in database"));

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password)); // Hash password
        user.setPhoneNumber(phoneNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(driverRole);
        user.setStatus("ACTIVE");

        // Save user first - this will generate the UUID
        User savedUser = userRepository.save(user);
        
        // Flush to ensure user is persisted and ID is available
        userRepository.flush();
        
        // Verify user has an ID
        UUID userId = savedUser.getUserId();
        if (userId == null) {
            throw new IllegalStateException("User ID was not generated after save");
        }

        // Set license expiry to 1 year from now (can be updated later)
        LocalDate licenseExpiry = LocalDate.now().plusYears(1);

        // Create driver record using native query to avoid Hibernate relationship issues
        driverRepository.insertDriver(userId, licenseNumber, licenseExpiry);
        driverRepository.flush();

        // Parse vehicle model to extract make and model
        // Format: "Make Model" or just "Model"
        String vehicleMake = "Unknown";
        String modelOnly = vehicleModel;
        if (vehicleModel != null && vehicleModel.contains(" ")) {
            String[] parts = vehicleModel.split(" ", 2);
            vehicleMake = parts[0];
            modelOnly = parts.length > 1 ? parts[1] : vehicleModel;
        }

        // Use current year as default (can be updated later)
        int vehicleYear = LocalDate.now().getYear();

        // Get the driver entity to set relationship
        var driverOpt = driverRepository.findByDriverId(userId);
        if (driverOpt.isEmpty()) {
            throw new IllegalStateException("Driver was not created successfully");
        }

        // Create vehicle record
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(driverOpt.get());
        vehicle.setVehicleMake(vehicleMake);
        vehicle.setVehicleModel(modelOnly);
        vehicle.setVehicleYear(vehicleYear);
        vehicle.setLicensePlate(vehiclePlate);
        vehicle.setIsActive(true);
        
        vehicleRepository.saveAndFlush(vehicle);

        return savedUser;
    }

    /**
     * Authenticate user and return User if credentials are valid
     */
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check if account is active
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalStateException("Account is not active");
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    /**
     * Get user by ID
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Convert RoleEntity to Role enum
     */
    public Role getRoleEnum(User user) {
        String roleName = user.getRole().getRoleName();
        return Role.valueOf(roleName);
    }
}

