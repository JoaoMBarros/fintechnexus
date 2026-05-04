CREATE TABLE transactions(
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    source_account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    destination_account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    amount_cents BIGINT NOT NULL,
    type VARCHAR NOT NULL,
    status VARCHAR NOT NULL DEFAULT 'PENDING',
    description VARCHAR,
    idempotency_key VARCHAR UNIQUE NOT NULL,
    reversed_by_id UUID REFERENCES transactions(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_transactions_source_account_created ON transactions (source_account_id);
CREATE INDEX idx_transactions_status ON transactions (status);