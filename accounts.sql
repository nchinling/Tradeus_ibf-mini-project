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