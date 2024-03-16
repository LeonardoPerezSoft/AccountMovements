-- Tabla para la entidad AccountEntity
CREATE TABLE IF NOT EXISTS account (
    account_id SERIAL PRIMARY KEY,
    number_account VARCHAR(255) NOT NULL,
    initial_balance float,
    state BOOLEAN,
    type VARCHAR(255) NOT NULL,
    client_name VARCHAR(255),
    client_id VARCHAR(255)
);

-- Tabla para la entidad TransactionEntity
CREATE TABLE IF NOT EXISTS transaction (
    transaction_number SERIAL PRIMARY KEY,
    date DATE,
    movement_type VARCHAR(50) NOT NULL,
    amount float,
    balance float,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);