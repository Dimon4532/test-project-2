CREATE TABLE IF NOT EXISTS employees (
                                         id VARCHAR(255) PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         salary NUMERIC(38,2) NOT NULL,
                                         department VARCHAR(50) NOT NULL,
                                         employee_type VARCHAR(50) NOT NULL,
                                         candidates_interviewed INTEGER DEFAULT 0,
                                         training_hours INTEGER DEFAULT 0,
                                         bugs_found INTEGER DEFAULT 0,
                                         team_size INTEGER DEFAULT 0,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_department ON employees(department);
CREATE INDEX idx_employee_type ON employees(employee_type);