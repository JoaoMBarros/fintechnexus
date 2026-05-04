CREATE TABLE idempotency_keys(
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    key VARCHAR UNIQUE NOT NULL, -- THIS IS THE CLIENT IDEMPOTENCY KEY
    transaction_id UUID REFERENCES transactions(id) ON DELETE RESTRICT,
    response_snapshot JSONB, -- THE API RESPONSE FOR THIS KEY REQUEST
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_idempotency_expires_at ON idempotency_keys (expires_at);