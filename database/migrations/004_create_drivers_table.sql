-- Migration: Create drivers table
-- Description: Driver-specific information

CREATE TABLE IF NOT EXISTS drivers (
    driver_id UUID PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    license_expiry DATE NOT NULL,
    rating DECIMAL(3,2) DEFAULT NULL CHECK (rating >= 0 AND rating <= 5),
    total_rides INTEGER NOT NULL DEFAULT 0,
    total_earnings DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'OFFLINE' CHECK (status IN ('AVAILABLE', 'BUSY', 'OFFLINE')),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_drivers_status ON drivers(status);
CREATE INDEX IF NOT EXISTS idx_drivers_is_verified ON drivers(is_verified);
CREATE INDEX IF NOT EXISTS idx_drivers_rating ON drivers(rating);

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_drivers_updated_at BEFORE UPDATE ON drivers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

