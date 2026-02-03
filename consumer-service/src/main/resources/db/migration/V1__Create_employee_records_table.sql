CREATE TABLE IF NOT EXISTS employee_records
(
    id              BIGSERIAL PRIMARY KEY,
    employee_name   VARCHAR(255) NOT NULL,
    status          VARCHAR(255) NOT NULL,
    event_timestamp BIGINT,
    received_at     TIMESTAMP    NOT NULL
);

CREATE INDEX idx_employee_name ON employee_records (employee_name);
CREATE INDEX idx_status ON employee_records (status);
CREATE INDEX idx_received_at ON employee_records (received_at);