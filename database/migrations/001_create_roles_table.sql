-- Migration: Create roles table
-- Description: Stores user roles (RIDER, DRIVER, ADMIN)

CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    CONSTRAINT chk_role_name CHECK (role_name IN ('RIDER', 'DRIVER', 'ADMIN'))
);

-- Create index on role_name for faster lookups
CREATE INDEX IF NOT EXISTS idx_roles_role_name ON roles(role_name);

-- Insert default roles
INSERT INTO roles (role_name, description) VALUES
    ('RIDER', 'Rider role for passengers'),
    ('DRIVER', 'Driver role for ride providers'),
    ('ADMIN', 'Administrator role')
ON CONFLICT (role_name) DO NOTHING;

