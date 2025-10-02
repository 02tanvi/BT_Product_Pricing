-- ========================
-- DATABASE: FD_Product_Pricing
-- ========================
CREATE DATABASE IF NOT EXISTS FD_Product_Pricing;
USE FD_Product_Pricing;

-- ========================
-- TABLE: PRODUCTS
-- ========================
CREATE TABLE products (
    product_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code    VARCHAR(50) UNIQUE NOT NULL,
    product_name    VARCHAR(100) NOT NULL,
    product_type    VARCHAR(50) NOT NULL,
    effective_date  DATE NOT NULL,
    expiry_date     DATE,
    branch          VARCHAR(50),
    currency        VARCHAR(10),
    status          VARCHAR(20),
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ========================
-- TABLE: BUSINESS_RULES
-- ========================
CREATE TABLE business_rules (
    rule_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    min_term        INT,
    max_term        INT,
    min_amount      DECIMAL(15,2),
    max_amount      DECIMAL(15,2),
    interest_rate   DECIMAL(5,2),
    compounding_frequency VARCHAR(50),
    premature_withdrawal_allowed BOOLEAN,
    premature_penalty_rate DECIMAL(5,2),
    auto_renewal    BOOLEAN,
    min_balance_required DECIMAL(15,2),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_business_rules_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ========================
-- TABLE: ROLES
-- ========================
CREATE TABLE roles (
    role_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    role_name       VARCHAR(50) NOT NULL,
    role_description VARCHAR(255),
    is_mandatory    BOOLEAN DEFAULT FALSE,
    max_count       INT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_roles_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ========================
-- TABLE: BALANCES
-- ========================
CREATE TABLE balances (
    balance_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    balance_type    VARCHAR(50) NOT NULL,
    balance_description VARCHAR(255),
    is_primary      BOOLEAN DEFAULT FALSE,
    calculation_method VARCHAR(50),
    min_balance_threshold DECIMAL(15,2),
    max_balance_limit DECIMAL(15,2),
    affect_interest_calculation BOOLEAN,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_balances_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ========================
-- TABLE: TRANSACTION_TYPES
-- ========================
CREATE TABLE transaction_types (
    txn_type_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    balance_id      BIGINT,
    txn_name        VARCHAR(50) NOT NULL,
    txn_description VARCHAR(255),
    is_debit        BOOLEAN,
    is_allowed      BOOLEAN,
    min_amount      DECIMAL(15,2),
    max_amount      DECIMAL(15,2),
    daily_limit     DECIMAL(15,2),
    frequency_limit_per_day INT,
    requires_min_balance_check BOOLEAN,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_txn_product FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT fk_txn_balance FOREIGN KEY (balance_id) REFERENCES balances(balance_id)
);

-- ========================
-- TABLE: CHARGES
-- ========================
CREATE TABLE charges (
    charge_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    balance_id      BIGINT,
    txn_type_id     BIGINT,
    charge_type     VARCHAR(50),
    charge_name     VARCHAR(100),
    rate            DECIMAL(8,2),
    calculation_type VARCHAR(50),
    frequency       VARCHAR(50),
    is_mandatory    BOOLEAN,
    threshold_amount DECIMAL(15,2),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_charges_product FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT fk_charges_balance FOREIGN KEY (balance_id) REFERENCES balances(balance_id),
    CONSTRAINT fk_charges_txn FOREIGN KEY (txn_type_id) REFERENCES transaction_types(txn_type_id)
);

-- ========================
-- TABLE: TRANSACTIONAL_BALANCE_RULES
-- ========================
CREATE TABLE transactional_balance_rules (
    rule_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    txn_type_id     BIGINT NOT NULL,
    balance_id      BIGINT NOT NULL,
    min_balance_after_txn DECIMAL(15,2),
    max_withdrawal_percentage DECIMAL(5,2),
    check_before_transaction BOOLEAN,
    validation_rule VARCHAR(255),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tbr_txn FOREIGN KEY (txn_type_id) REFERENCES transaction_types(txn_type_id),
    CONSTRAINT fk_tbr_balance FOREIGN KEY (balance_id) REFERENCES balances(balance_id)
);

-- ========================
-- TABLE: CHARGE_BALANCE_MAPPING
-- ========================
CREATE TABLE charge_balance_mapping (
    mapping_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    charge_id       BIGINT NOT NULL,
    balance_id      BIGINT NOT NULL,
    txn_type_id     BIGINT,
    application_rule VARCHAR(255),
    balance_threshold DECIMAL(15,2),
    is_active       BOOLEAN,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cbm_charge FOREIGN KEY (charge_id) REFERENCES charges(charge_id),
    CONSTRAINT fk_cbm_balance FOREIGN KEY (balance_id) REFERENCES balances(balance_id),
    CONSTRAINT fk_cbm_txn FOREIGN KEY (txn_type_id) REFERENCES transaction_types(txn_type_id)
);

-- ========================
-- TABLE: AUDITS
-- ========================
CREATE TABLE audits (
    audit_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT,
    affected_entity_id BIGINT,
    entity_type     VARCHAR(50),
    operation       VARCHAR(50),
    old_values      TEXT,
    new_values      TEXT,
    created_by      VARCHAR(50),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address      VARCHAR(50),
    remarks         TEXT,
    CONSTRAINT fk_audits_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ========================
-- STEP 1: DROP OLD AUDIT TABLE
-- ========================
DROP TABLE IF EXISTS audits;

-- ========================
-- STEP 2: REMOVE AUDIT COLUMNS FROM BUSINESS TABLES
-- ========================

-- PRODUCTS
ALTER TABLE products
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- BUSINESS_RULES
ALTER TABLE business_rules
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- ROLES
ALTER TABLE roles
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- BALANCES
ALTER TABLE balances
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- TRANSACTION_TYPES
ALTER TABLE transaction_types
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- CHARGES
ALTER TABLE charges
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- TRANSACTIONAL_BALANCE_RULES
ALTER TABLE transactional_balance_rules
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- CHARGE_BALANCE_MAPPING
ALTER TABLE charge_balance_mapping
    DROP COLUMN created_at;

-- ========================
-- STEP 3: CREATE NEW CENTRALIZED AUDITS TABLE
-- ========================
CREATE TABLE audits (
    audit_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_name     VARCHAR(100) NOT NULL,  -- e.g. "products", "charges"
    entity_id       BIGINT NOT NULL,        -- the row being changed
    operation       VARCHAR(50) NOT NULL,   -- INSERT, UPDATE, DELETE
    old_values      TEXT,
    new_values      TEXT,
    changed_by      VARCHAR(50),
    changed_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address      VARCHAR(50),
    remarks         TEXT
);
INSERT INTO products (product_code, product_name, product_type, effective_date, branch, currency, status, description) VALUES
('GYFD', 'Golden Years Fixed Deposit', 'Fixed Deposit', '2025-01-01', 'All Branches', 'INR', 'Active', 'Exclusive FD for Senior Citizens (60+)'),
('BPA', 'Bankers’ Privilege Account', 'Savings Account', '2025-01-01', 'All Branches', 'INR', 'Active', 'Special Account for Bank Employees'),
('CSFD', 'Classic Saver Fixed Deposit', 'Fixed Deposit', '2025-01-01', 'All Branches', 'INR', 'Active', 'Standard FD for Retail Customers'),
('SSA', 'Student Savings Account', 'Savings Account', '2025-01-01', 'All Branches', 'INR', 'Active', 'Zero balance savings account for students'),
('EHAL', 'EcoHome Advantage Loan', 'Loan', '2025-01-01', 'All Branches', 'INR', 'Active', 'Green housing loan for sustainable living'),
('EIL', 'ESG Impact Loan', 'Loan', '2025-01-01', 'Corporate Banking', 'INR', 'Active', 'Sustainability-linked corporate loan');

INSERT INTO roles (product_id, role_name, role_description, is_mandatory, max_count) VALUES
-- GYFD (1)
(1, 'Owner', 'Primary account holder', TRUE, 1),
(1, 'Nominee', 'Person who receives the deposit in case of death', FALSE, 1),
(1, 'Guardian', 'Guardian for senior if required', FALSE, 1),

-- BPA (2)
(2, 'Owner', 'Primary account holder', TRUE, 1),
(2, 'Co-Owner', 'Secondary account holder', FALSE, 2),
(2, 'Nominee', 'Nominee for the account', FALSE, 1),

-- CSFD (3)
(3, 'Owner', 'Primary account holder', TRUE, 1),
(3, 'Nominee', 'Nominee for the deposit', FALSE, 1),

-- SSA (4)
(4, 'Owner', 'Primary account holder', TRUE, 1),
(4, 'Guardian', 'Guardian if student is minor', FALSE, 1),
(4, 'Nominee', 'Nominee for the account', FALSE, 1),

-- EHAL (5)
(5, 'Borrower', 'Loan applicant', TRUE, 1),
(5, 'Guarantor', 'Guarantor if required', FALSE, 1),
(5, 'Co-Owner', 'Joint loan holder if applicable', FALSE, 2),

-- EIL (6)
(6, 'Borrower', 'Loan applicant', TRUE, 1),
(6, 'Guarantor', 'Guarantor if required', FALSE, 1);

INSERT INTO business_rules
(product_id, min_term, max_term, min_amount, max_amount, interest_rate, compounding_frequency, premature_withdrawal_allowed, premature_penalty_rate, auto_renewal, min_balance_required)
VALUES
-- GYFD (product_id = 1)
(1, 1, 10, 10000, 2500000, 0.5, 'Quarterly', TRUE, 0.5, TRUE, 0),

-- BPA (product_id = 2)
(2, NULL, NULL, 0, NULL, 1.0, NULL, TRUE, 0, NULL, 0),

-- CSFD (product_id = 3)
(3, 0, 120, 5000, NULL, 0, 'Quarterly', TRUE, 1.0, NULL, NULL),

-- SSA (product_id = 4)
(4, 6, 36, 0, 0, 0.25, NULL, TRUE, 0, NULL, 0),

-- EHAL (product_id = 5)
(5, NULL, NULL, 1000000, 50000000, -0.25, NULL, TRUE, 0, TRUE, NULL),

-- EIL (product_id = 6)
(6, NULL, NULL, 500000000, NULL, -0.25, NULL, TRUE, 0, TRUE, NULL);

select * from business_rules;
