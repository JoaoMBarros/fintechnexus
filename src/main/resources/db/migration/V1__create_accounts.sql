-- CREATING A UUID7 BECAUSE UUID7 USES CRONOLOGICAL ORDERING

CREATE OR REPLACE FUNCTION uuidv7() RETURNS uuid AS $$
DECLARE
unix_ms bigint := (extract(epoch from clock_timestamp()) * 1000)::bigint;
  hex     text;
BEGIN
  hex :=
    lpad(to_hex(unix_ms), 12, '0') ||
    '7' ||
    lpad(to_hex((random() * x'fff'::bigint)::bigint), 3, '0') ||
    to_hex(8 + (random() * 3)::int) ||
    lpad(to_hex((random() * x'0fffffffffffffff'::bigint)::bigint), 15, '0');

RETURN (
    substring(hex,  1, 8) || '-' ||
    substring(hex,  9, 4) || '-' ||
    substring(hex, 13, 4) || '-' ||
    substring(hex, 17, 4) || '-' ||
    substring(hex, 21, 12)
    )::uuid;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    document_number VARCHAR UNIQUE NOT NULL,
    holder_name VARCHAR NOT NULL,
    email VARCHAR UNIQUE,
    account_type VARCHAR NOT NULL,
    balance_cents BIGINT DEFAULT 0 CHECK ( balance_cents >= 0 ),
    status VARCHAR NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_accounts_status ON accounts (status);