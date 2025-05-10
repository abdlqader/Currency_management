-- This migration creates the 'currency' table with timestamps for creation and updates.
CREATE TABLE IF NOT EXISTS currencies (
    id UUID PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,    -- Currency code (e.g., USD, EUR)
    name VARCHAR(255) NOT NULL,          -- Currency name (e.g., US Dollar, Euro)
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,  -- Timestamp for when the record is created
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP   -- Timestamp for when the record is last updated
);
