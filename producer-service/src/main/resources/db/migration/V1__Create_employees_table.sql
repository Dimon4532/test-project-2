CREATE TABLE IF NOT EXISTS employees (
                                         id VARCHAR(255) PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         salary DOUBLE PRECISION NOT NULL,
                                         department VARCHAR(50) NOT NULL,
                                         employee_type VARCHAR(50) NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_department ON employees(department);
CREATE INDEX idx_employee_type ON employees(employee_type);