//create database
create database trade_account;
use trade_account;

//create table
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

-- product_list_id INT, 

//populate with data
insert into accounts(account_id,name, username, password, mobile_no, nationality ) VALUES

-- ('abcde11111', 'CL Ng', 100000.45),
-- ('abcde22222', 'Ms Seet', 999999.99),
-- ('abcde33333', 'Woo Young Woo', 43501.27),
-- ('abcde44444', 'Moon Dong Eun', 50000),
-- ('abcde55555', 'Morgan Redman', 1200000);
