DROP TABLE IF EXISTS SECURITY CASCADE;
CREATE TABLE SECURITY
(
   SecurityId IDENTITY PRIMARY KEY,
   ISIN VARCHAR (50) NOT NULL,
   Ticker VARCHAR (50) NOT NULL,
   Cusip VARCHAR (50) NOT NULL
) AS SELECT * FROM CSVREAD('classpath:securities.csv', null, 'charset=UTF-8 fieldSeparator=,');

DROP TABLE IF EXISTS PORTFOLIO CASCADE;
CREATE TABLE PORTFOLIO
(
   PortfolioId IDENTITY PRIMARY KEY,
   PortfolioCode VARCHAR (50) NOT NULL
) AS SELECT * FROM CSVREAD('classpath:portfolios.csv', null, 'charset=UTF-8 fieldSeparator=,');

DROP TABLE IF EXISTS TRANSACTION CASCADE;
CREATE TABLE TRANSACTION
(
   --TransactionId IDENTITY NOT NULL PRIMARY KEY,
   SecurityId INT,
   PortfolioId INT,
   Nominal VARCHAR (50) NOT NULL,
   OMS VARCHAR (3) NOT NULL,
   TransactionType VARCHAR (50) NOT NULL,
   FOREIGN KEY (SecurityId) REFERENCES SECURITY(SecurityId),
   FOREIGN KEY (PortfolioId) REFERENCES PORTFOLIO(PortfolioId)
) AS SELECT * FROM CSVREAD('classpath:transactions.csv', null, 'charset=UTF-8 fieldSeparator=,');