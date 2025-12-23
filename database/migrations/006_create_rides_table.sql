-- Migration: Create rides table
-- Description: Ride records

CREATE TABLE IF NOT EXISTS rides (
    ride_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rider_id UUID NOT NULL REFERENCES riders(rider_id) ON DELETE CASCADE,
    driver_id UUID REFERENCES drivers(driver_id) ON DELETE SET NULL,
    vehicle_id UUID REFERENCES vehicles(vehicle_id) ON DELETE SET NULL,
    pickup_latitude DECIMAL(10,8) NOT NULL,
    pickup_longitude DECIMAL(11,8) NOT NULL,
    pickup_address VARCHAR(500) NOT NULL,
    dropoff_latitude DECIMAL(10,8),
    dropoff_longitude DECIMAL(11,8),
    dropoff_address VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    estimated_fare DECIMAL(10,2),
    actual_fare DECIMAL(10,2),
    estimated_distance DECIMAL(8,2),
    actual_distance DECIMAL(8,2),
    estimated_duration INTEGER,
    actual_duration INTEGER,
    cancelled_by VARCHAR(10) CHECK (cancelled_by IN ('RIDER', 'DRIVER')),
    cancellation_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_rides_rider_id ON rides(rider_id);
CREATE INDEX IF NOT EXISTS idx_rides_driver_id ON rides(driver_id);
CREATE INDEX IF NOT EXISTS idx_rides_vehicle_id ON rides(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_rides_status ON rides(status);
CREATE INDEX IF NOT EXISTS idx_rides_created_at ON rides(created_at);
CREATE INDEX IF NOT EXISTS idx_rides_rider_status ON rides(rider_id, status);
CREATE INDEX IF NOT EXISTS idx_rides_driver_status ON rides(driver_id, status);
CREATE INDEX IF NOT EXISTS idx_rides_status_created ON rides(status, created_at);

-- Create trigger to automatically update updated_at (if needed in future)
-- Note: rides table doesn't have updated_at, but we can add it later if needed

