CREATE TABLE sync_event (
    id UUID PRIMARY KEY,
    source_event_id UUID NOT NULL UNIQUE,
    event_type VARCHAR(120) NOT NULL,
    payload JSONB NOT NULL,
    received_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMPTZ
);
