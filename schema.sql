-- create database
create database trade_account;
use trade_account;

-- table for user account
create table accounts(
account_id VARCHAR(10) NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
username VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL,
mobile_no VARCHAR(20) NOT NULL,
nationality VARCHAR(40) NOT NULL,
date_of_birth DATE, 
address VARCHAR(50)
);

-- table for user portfolio
-- CREATE TABLE portfolio (
--     id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--     account_id VARCHAR(10) NOT NULL,
--     symbol VARCHAR(10),
--     INDEX idx_portfolio_symbol_account (symbol, account_id)
-- );

CREATE TABLE portfolio (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_id VARCHAR(10) NOT NULL,
    symbol VARCHAR(10),
    CONSTRAINT uc_portfolio_symbol_account UNIQUE (symbol, account_id)
);

CREATE TABLE trades (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    portfolio_id INT NOT NULL,
    account_id VARCHAR(10) NOT NULL, 
    username VARCHAR(50) NOT NULL,
    exchange VARCHAR(10),
    symbol VARCHAR(10),
    stock_name VARCHAR(50),
    units NUMERIC(15,2),
    buy_date DATE,
    buy_price NUMERIC(8,2),
    currency VARCHAR(10),
    fee NUMERIC(10,2),
    total NUMERIC(15,2),
    FOREIGN KEY (portfolio_id) REFERENCES portfolio(id) ON DELETE CASCADE
);






-- -- table for user trades
-- CREATE TABLE trades (
--     id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--     account_id VARCHAR(10) NOT NULL, 
--     username VARCHAR(50) NOT NULL,  
--     exchange VARCHAR(10),
--     symbol VARCHAR(10),
--     stock_name VARCHAR(50),
--     units NUMERIC(15,2),
--     buy_date DATE,
--     buy_price NUMERIC(8,2),
--     currency VARCHAR(10),
--     fee NUMERIC(10,2),
--     total NUMERIC(15,2)
-- );


-- -- table for user trades
-- CREATE TABLE trades (
--     id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--     account_id VARCHAR(10) NOT NULL, 
--     username VARCHAR(50) NOT NULL,  
--     exchange VARCHAR(10),
--     symbol VARCHAR(10),
--     stock_name VARCHAR(50),
--     units NUMERIC(15,2),
--     buy_date DATE,
--     buy_price NUMERIC(8,2),
--     currency VARCHAR(10),
--     fee NUMERIC(10,2),
--     total NUMERIC(15,2),
--     FOREIGN KEY (symbol, account_id) REFERENCES portfolio(symbol, account_id)
-- );

