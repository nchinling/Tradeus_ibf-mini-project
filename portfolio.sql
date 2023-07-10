CREATE TABLE portfolio (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_id VARCHAR(10) NOT NULL,
    symbol VARCHAR(10),
    CONSTRAINT uc_portfolio_symbol_account UNIQUE (symbol, account_id)
);