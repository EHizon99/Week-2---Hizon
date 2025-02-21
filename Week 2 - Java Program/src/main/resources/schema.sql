-- Store account information
CREATE TABLE accounts (
    account_id VARCHAR(10) PRIMARY KEY,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

-- Store all transactions
CREATE TABLE transactions (
    account_id VARCHAR(10),
    amount DECIMAL(10,2),
    transaction_date TIMESTAMP,
    FOREIGN KEY (account_id)
        REFERENCES accounts(account_id)
);
