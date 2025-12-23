-- Migration: Create riders table
-- Description: Rider-specific information

CREATE TABLE IF NOT EXISTS riders (
    rider_id UUID PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    rating DECIMAL(3,2) DEFAULT NULL CHECK (rating >= 0 AND rating <= 5),
    total_rides INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on rating for sorting/filtering
CREATE INDEX IF NOT EXISTS idx_riders_rating ON riders(rating);

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_riders_updated_at BEFORE UPDATE ON riders
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

