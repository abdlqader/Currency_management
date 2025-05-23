-- This migration creates the 'exchange_rate' table with timestamps for creation and updates.
CREATE TABLE IF NOT EXISTS exchange_rates (
    id UUID PRIMARY KEY,
    source_currency_id UUID NOT NULL,      -- Foreign key to currency (source)
    target_currency_code VARCHAR(3) NOT NULL,      -- Foreign key to currency (target)
    rate DOUBLE PRECISION NOT NULL,       -- Exchange rate value
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,  -- Timestamp for when the record is created
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,  -- Timestamp for when the record is last updated
    FOREIGN KEY (source_currency_id) REFERENCES currencies(id) ON DELETE CASCADE
);
